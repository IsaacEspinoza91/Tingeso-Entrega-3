package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.ClienteReserva;
import com.kartingrm.reservas_comprobantes_service.entity.Comprobante;
import com.kartingrm.reservas_comprobantes_service.entity.DetalleComprobante;
import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.*;
import com.kartingrm.reservas_comprobantes_service.modelbase.BaseService;
import com.kartingrm.reservas_comprobantes_service.repository.ComprobanteRepository;
import com.kartingrm.reservas_comprobantes_service.repository.DetalleComprobanteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComprobanteService extends BaseService {

    // Constantes
    private static final double IVA = 0.19;

    private final ComprobanteRepository comprobanteRepository;
    private final DetalleComprobanteRepository detalleComprobanteRepository;      // Entidad débil depende de comprobante
    private final ClienteReservaService clienteReservaService;

    public ComprobanteService(ComprobanteRepository comprobanteRepository,
                              DetalleComprobanteRepository detalleComprobanteRepository,
                              ClienteReservaService clienteReservaService,
                              RestTemplate restTemplate) {
        super(restTemplate);
        this.comprobanteRepository = comprobanteRepository;
        this.detalleComprobanteRepository = detalleComprobanteRepository;
        this.clienteReservaService = clienteReservaService;
    }



    // GET. Obtener comprobante con detalles segun id comprobante
    public ComprobanteConDetallesDTO getComprobanteConDetallesById(Long idComprobante) {
        Comprobante comprobante = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new EntityNotFoundException("Comprobante de ID " + idComprobante + " no encontrado"));

        // Obtener los detalles del comprobante
        List<DetalleComprobante> detallesOriginales = detalleComprobanteRepository.findDetalleComprobantesByIdComprobante(idComprobante);

        List<DetalleComprobanteConClienteDTO> detalles = new ArrayList<>();
        for (DetalleComprobante detalleActual : detallesOriginales) {
            ClienteDTO integranteObjeto = restTemplate.getForObject(CLIENTE_ENDPOINT + detalleActual.getIdCliente(), ClienteDTO.class);
            detalles.add(new DetalleComprobanteConClienteDTO(detalleActual,integranteObjeto));
        }

        Reserva reserva = obtenerReserva(comprobante.getIdReserva());
        ClienteDTO cliente = obtenerCliente(reserva.getIdReservante());
        PlanDTO plan = obtenerPlan(reserva.getIdPlan());
        ReservaDTO reservaDTO = new ReservaDTO(reserva, plan, cliente);

        return new ComprobanteConDetallesDTO(comprobante, reservaDTO, detalles);
    }


    // GET.  Obtener comprobante con detalles segun id reserva
    public ComprobanteConDetallesDTO getComprobanteConDetallesByIdReserva(Long idReserva) {
        Comprobante comprobante = comprobanteRepository.findByIdReserva(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Comprobante no encontrado segun la reserva ID: " + idReserva));

        // Obtener los detalles del comprobante
        List<DetalleComprobante> detallesOriginales = detalleComprobanteRepository
                .findDetalleComprobantesByIdComprobante(comprobante.getId());

        List<DetalleComprobanteConClienteDTO> detalles = new ArrayList<>();
        for (DetalleComprobante detalleActual : detallesOriginales) {
            ClienteDTO integranteObjeto = restTemplate.getForObject(CLIENTE_ENDPOINT+ detalleActual.getIdCliente(), ClienteDTO.class);
            detalles.add(new DetalleComprobanteConClienteDTO(detalleActual,integranteObjeto));
        }

        Reserva reserva = obtenerReserva(idReserva);
        ClienteDTO cliente = obtenerCliente(reserva.getIdReservante());
        PlanDTO plan = obtenerPlan(reserva.getIdPlan());
        ReservaDTO reservaDTO = new ReservaDTO(reserva, plan, cliente);

        return new ComprobanteConDetallesDTO(comprobante, reservaDTO, detalles);
    }



    // Create. Crear comprobante completo con generacion automatica de detalles por cliente. Transactional, no usa save
    @Transactional
    public ComprobanteConDetallesDTO createComprobante(Long reservaId, double descuentoExtra) {
        // Validaciones iniciales
        if (reservaId == null) {
            throw new IllegalArgumentException("ID de reserva no puede ser nulo");
        }
        if (descuentoExtra < 0) {
            throw new IllegalArgumentException("Descuento extra no puede ser negativo");
        }
        if (comprobanteRepository.findByIdReserva(reservaId).isPresent()) {
            throw new IllegalArgumentException("Ya existe un comprobante asociado a esta reserva");
        }

        // Obtener datos principales
        Reserva reserva = obtenerReserva(reservaId);
        if (reserva == null) {
            throw new EntityNotFoundException("Reserva no encontrada");
        }
        if (reserva.getTotalPersonas() <= 0) {
            throw new IllegalStateException("La reserva debe tener al menos una persona");
        }

        PlanDTO plan = obtenerPlan(reserva.getIdPlan());
        ClienteDTO cliente = obtenerCliente(reserva.getIdReservante());
        List<ClienteReserva> integrantes = clienteReservaService.obtenerIntegrantesByIdReserva(reservaId);

        // Validar integridad de datos
        if (integrantes.isEmpty()) {
            throw new IllegalStateException("No hay clientes asociados a la reserva");
        }
        if (integrantes.size() != reserva.getTotalPersonas()) {
            throw new IllegalStateException(
                    String.format("Cantidad de integrantes (%d) no coincide con total de personas (%d) en la reserva",
                            integrantes.size(), reserva.getTotalPersonas())
            );
        }


        // Crear objeto comprobante, asignar reserva y estado pagado
        Comprobante comprobante = new Comprobante(null, true, reservaId);
        comprobante = comprobanteRepository.save(comprobante); // Guardar primero para obtener ID

        // Procesar detalles
        double tarifaBase = calcularTarifaBase(reserva.getFecha(), reserva.getIdPlan());
        int totalPersonas = reserva.getTotalPersonas();
        double tarifaIntegrante = tarifaBase / totalPersonas;
        double descuentoExtraIntegrante = descuentoExtra / totalPersonas;


        // Crear detalles para cada persona en la reserva
        List<DetalleComprobanteConClienteDTO> detalles = crearDetallesDeComprobante(
                integrantes, reserva, totalPersonas, descuentoExtraIntegrante, tarifaIntegrante, comprobante.getId());

        actualizarTotalComprobante(comprobante);
        notificarActualizacionDeReportes(reserva, comprobante, plan, true);

        ReservaDTO reservaDTO = new ReservaDTO(reserva, plan, cliente);
        return new ComprobanteConDetallesDTO(comprobante, reservaDTO, detalles);
    }



    // Crear DetalleComprobante considerando generacion de precios automaticamente
    private DetalleComprobante createDetalleComprobante(Long idComprobante,
                                                       Long idCliente,
                                                       double tarifa,
                                                       double descuentoExtra,
                                                       int totalPersonas,
                                                       LocalDate fechaReserva,
                                                       int cantidadCumpleanieros) {

        // Creo nuevo detalle. BUILDER
        DetalleComprobante detalle = new DetalleComprobante();
        detalle.setIdComprobante(idComprobante);
        detalle.setIdCliente(idCliente);


        double porcentajeDescuentoGrupo = calcularDescuentoGrupo(totalPersonas);
        double porcentajeDescuentoEspecial = calcularDescuentoEspecial(idCliente, fechaReserva, totalPersonas,cantidadCumpleanieros, detalle);

        // Calculamos el monto con descuentos. Utilizamos descuentos en cascada, es decir, el descuento siguiente
        // se realiza sobre el valor anterior con descuento, no sobre el valor original
        double descuentoGrupo = tarifa * porcentajeDescuentoGrupo;
        double descuentoEspecial = (tarifa - descuentoGrupo) * porcentajeDescuentoEspecial;
        double montoConDescuento = tarifa - descuentoGrupo - descuentoEspecial - descuentoExtra;// monto (sin iva)
        double iva = montoConDescuento * IVA;
        double total = montoConDescuento + iva;


        // Setear valores dentro del detalle, considerando descuento de cumpleanios y especiales
        detalle.setTarifa(tarifa);
        detalle.setDescuentoGrupo(descuentoGrupo);
        detalle.setDescuentoEspecial(descuentoEspecial);
        detalle.setDescuentoExtra(descuentoExtra);
        detalle.setMontoFinal(montoConDescuento);
        detalle.setMontoIva(iva);
        detalle.setMontoTotal(total);
        detalle.setPorcentajeDescuentoGrupo(porcentajeDescuentoGrupo*100);
        detalle.setPorcentajeDescuentoEspecial(porcentajeDescuentoEspecial*100);

        return detalle;
    }



    // UPDATE. Permite cambiar el estado (BOOL) de un comprobante
    public Comprobante updateEstadoPagadoDeComprobante(Long idComprobante, Boolean pagado) {
        Comprobante comprobante = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new EntityNotFoundException("Comprobante no encontrado"));

        comprobante.setPagado(pagado);

        Reserva reserva = obtenerReserva(comprobante.getIdReserva());
        PlanDTO plan = obtenerPlan(reserva.getIdPlan());

        // Actualización de reporte según estado de reserva
        notificarActualizacionDeReportes(reserva, comprobante, plan, comprobante.isPagado());

        return comprobanteRepository.save(comprobante);
    }


    // DELETE
    public boolean deleteComprobante(Long idComprobante) {
        List<DetalleComprobante> detallesOriginales = detalleComprobanteRepository.findDetalleComprobantesByIdComprobante(idComprobante);
        detalleComprobanteRepository.deleteAll(detallesOriginales);

        Comprobante comprobante = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new EntityNotFoundException("Comprobante no encontrado"));
        Reserva reserva = obtenerReserva(comprobante.getIdReserva());
        PlanDTO plan = obtenerPlan(reserva.getIdPlan());

        comprobanteRepository.deleteById(idComprobante);

        notificarActualizacionDeReportes(reserva, comprobante, plan, false);

        return true;
    }




    // Lógica interna

    // Notificar actualización de reportes en el microservicio
    private void notificarActualizacionDeReportes(Reserva reserva, Comprobante comprobante, PlanDTO plan, boolean esSuma) {

        // Reportes según rango de personas
        notificarReporteIngresosCantidadIntegrantesPeticionPOST(
                determinarRangoPersonas(reserva.getTotalPersonas()),
                reserva.getFecha(),
                comprobante.getTotal(),
                esSuma
        );

        // Reportes según planes
        notificarReporteIngresosPlan(
                plan.getId(),
                plan.getDescripcion(),
                reserva.getFecha(),
                comprobante.getTotal(),
                esSuma
        );

    }


    // Obtener que tipo de % descuento especial aplica (cliente frecuente o cumpleanios)
    private double calcularDescuentoEspecial(Long idCliente,
                                             LocalDate fechaReserva,
                                             int totalPersonas,
                                             int cantidadCumpleanieros,
                                             DetalleComprobante detalle) {
        if (esCumpleaniero(idCliente, fechaReserva)) {
            return calcularDescuentoCumpleaniero(idCliente, fechaReserva, totalPersonas, cantidadCumpleanieros, detalle);
        } else {
            return calcularDescuentoFrecuente(idCliente, detalle);
        }
    }

    private double calcularDescuentoCumpleaniero(Long idCliente,
                                                 LocalDate fechaReserva,
                                                 int totalPersonas,
                                                 int cantidadCumpleanieros,
                                                 DetalleComprobante detalle) {
        if (cumpleCondicionesDescuentoCumpleanios(totalPersonas, cantidadCumpleanieros)) {
            detalle.setTieneDescuentoCumpleanios(true);
            return calcularDescuentoCumpleanios(idCliente, fechaReserva);
        }
        return 0.0;
    }

    private double calcularDescuentoFrecuente(Long idCliente, DetalleComprobante detalle) {
        double descuento = calcularDescuentoFrecuenteByIdCliente(idCliente);
        if (descuento != 0.0) {
            detalle.setTieneDescuentoClienteFrecuente(true);
        }
        return descuento;
    }

    // Validación condición de descuento de cumpleaños.
    /*
       Reglas de negocio sobre cantidad maxima de descuentos cumpleaños (50%)
           Grupo de 3 a 5: 1 persona max de cumpleaños tiene descuento
           Grupo de 6 a 15: 2 personas max de cumpleaños tienen descuento
     */
    private boolean cumpleCondicionesDescuentoCumpleanios(int totalPersonas, int cantidadCumpleanieros) {
        boolean esGrupoPequeno = totalPersonas >= 3 && totalPersonas <= 5 && cantidadCumpleanieros < 1;
        boolean esGrupoGrande = totalPersonas >= 6 && totalPersonas <= 15 && cantidadCumpleanieros < 2;
        return esGrupoPequeno || esGrupoGrande;
    }



    // Obtiene porcentaje del descuento para clientes frecuentes
    /*
       Cliente frecuente:
           No frecuente (0-1): 0%  , Regular (2-4): 10%  , Frecuente (5-6): 20%  , Muy Frecuente (7 o mas): 30%
     */
    private double calcularDescuentoFrecuenteByIdCliente(Long idCliente) {
        try {
            Double descuento = restTemplate.getForObject(CLIENTE_RESERVA_ENDPOINT + "porcen-desc-by-visitas-cliente/" + idCliente, Double.class);
            if (descuento == null) throw new IllegalStateException("El servicio de descuento frecuente no devolvió respuesta válida");

            return descuento;
        } catch (ResourceAccessException ex) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Servicio de descuento frecuente no disponible: " + ex.getMessage());
        }

    }


    // Obtiene porcentaje de descuento según el número de personas del grupo
    // Petición al MC2
    public double calcularDescuentoGrupo(int totalPersonas) {
        try {
            Double descuento = restTemplate.getForObject(DESCUENTO_GRUPO_ENDPOINT + "cantidad/" + totalPersonas, Double.class);
            if (descuento == null) throw new IllegalStateException("El servicio de descuento de grupo no devolvió respuesta válida");

            return descuento;
        } catch (ResourceAccessException ex) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Servicio de descuento de grupo no disponible: " + ex.getMessage());
        }
    }


    // Determina porcentaje de descuento de cumpleanios si es que el cliente esta de cumpleanios el un dia de reserva
    // 50% de descuento para cumpleaniero
    private double calcularDescuentoCumpleanios(Long idCliente, LocalDate fecha) {
        if (esCumpleaniero(idCliente, fecha)) {
            return 0.5;
        } else {
            return 0.0;
        }
    }


    // Obtiene el precio de tarifa del arriendo según el día (semana, fin de semana o feriado)
    public double calcularTarifaBase(LocalDate fecha, Long idPlan) {
        boolean esFinDeSemana = fecha.getDayOfWeek().getValue() >= 6;
        boolean esFeriado = esDiaFeriado(fecha);
        PlanDTO plan = obtenerPlan(idPlan);

        if (esFeriado) return plan.getPrecioFeriado();
        else if (esFinDeSemana) return plan.getPrecioFinSemana();
        else return plan.getPrecioRegular();
    }


    // Obtiene booleano si es cumpleaniero el cliente
    public boolean esCumpleaniero(Long idCliente, LocalDate fecha) {
        try{
            Boolean result = restTemplate.getForObject(CLIENTE_CUMPLEANIOS_ENDPOINT + "cliente/" + idCliente + "/esCumpleaniero?fecha=" + fecha, Boolean.class);
            if (result == null) throw new IllegalStateException("El servicio de cumpleaños no devolvió respuesta válida");

            return result;
        } catch (ResourceAccessException ex) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Servicio de cumpleaños no disponible: " + ex.getMessage());
        }
    }


    // Calcular y actualizar el atributo total de Comprobante. Como es Transactional, no usa save
    @Transactional
    public void actualizarTotalComprobante(Comprobante comprobante) {
        double total = detalleComprobanteRepository
                .findDetalleComprobantesByIdComprobante(comprobante.getId())
                .stream()
                .mapToDouble(DetalleComprobante::getMontoTotal)
                .sum();

        comprobante.setTotal(total);
    }


    // Realizacion de peticion POST para crear o actualizar la tabla de reportes segun grupo en el micro servicio 7
    private void notificarReporteIngresosCantidadIntegrantesPeticionPOST(String rangoPersonas, LocalDate fecha, Double monto, boolean esSuma) {
        String url = REPORTES_RANGO_ENDPOINT + "actualizar" +
                "?rangoPersonas=" + rangoPersonas +
                "&fechaReserva=" + fecha.toString() +
                "&monto=" + monto +
                "&esSuma=" + esSuma;

        restTemplate.postForEntity(url, null, Void.class);
    }

    // Crea string para indicar el rango de personas
    public String determinarRangoPersonas(int cantidad) {
        if (cantidad <= 2) return "1-2 personas";
        if (cantidad <= 5) return "3-5 personas";
        if (cantidad <= 10) return "6-10 personas";
        return "11-15 personas";
    }


    // Realizacion de peticion POST para poblar tabla de reportes segun plan en el microservicio 7
    private void notificarReporteIngresosPlan(Long idPlan, String descripcionPlan, LocalDate fecha, Double monto, boolean esSuma) {
        String url = REPORTES_PLAN_ENDPOINT+ "actualizar" +
                "?idPlan=" + idPlan +
                "&descripcionPlan=" + descripcionPlan +
                "&fechaReserva=" + fecha.toString() +
                "&monto=" + monto +
                "&esSuma=" + esSuma;

        restTemplate.postForEntity(url, null, Void.class);
    }


    // Crear detalles para cada persona en la reserva. Iterar sobre la lista de integrantes
    private List<DetalleComprobanteConClienteDTO> crearDetallesDeComprobante(List<ClienteReserva> integrantes,
                                                                             Reserva reserva,
                                                                             int totalPersonas,
                                                                             double descuenteExtraIntegrante,
                                                                             double tarifaIntegrante,
                                                                             Long idComprobante) {
        int cantidadCumpleanieros = 0;
        List<DetalleComprobanteConClienteDTO> detalles = new ArrayList<>();

        for (ClienteReserva clienteActual : integrantes) {

            DetalleComprobante detalle = createDetalleComprobante(
                    idComprobante,
                    clienteActual.getIdCliente(),
                    tarifaIntegrante,
                    descuenteExtraIntegrante,
                    totalPersonas,
                    reserva.getFecha(),
                    cantidadCumpleanieros);

            detalleComprobanteRepository.save(detalle);

            ClienteDTO integranteObjeto = restTemplate.getForObject(CLIENTE_ENDPOINT + clienteActual.getIdCliente(), ClienteDTO.class);
            detalles.add(new DetalleComprobanteConClienteDTO(detalle, integranteObjeto));

            // Si es cumpleaniero, se suma a la cantidad en la variable
            if (esCumpleaniero(clienteActual.getIdCliente(), reserva.getFecha())) cantidadCumpleanieros += 1;

        }
        return detalles;
    }


}
