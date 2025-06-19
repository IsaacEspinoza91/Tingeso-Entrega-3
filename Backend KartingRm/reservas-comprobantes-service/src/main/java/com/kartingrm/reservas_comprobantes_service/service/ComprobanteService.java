package com.kartingrm.reservas_comprobantes_service.service;

import com.kartingrm.reservas_comprobantes_service.entity.ClienteReserva;
import com.kartingrm.reservas_comprobantes_service.entity.Comprobante;
import com.kartingrm.reservas_comprobantes_service.entity.DetalleComprobante;
import com.kartingrm.reservas_comprobantes_service.entity.Reserva;
import com.kartingrm.reservas_comprobantes_service.model.*;
import com.kartingrm.reservas_comprobantes_service.repository.ComprobanteRepository;
import com.kartingrm.reservas_comprobantes_service.repository.DetalleComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComprobanteService {

    private static final double IVA = 0.19;         // Valor porcentual constante del IVA

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private ComprobanteRepository comprobanteRepository;
    @Autowired
    private DetalleComprobanteRepository detalleComprobanteRepository;      // Entidad debil depende de comprobante
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ClienteReservaService clienteReservaService;



    // GET
    // Obtener comprobante con detalles segun id comprobante
    public ComprobanteConDetallesDTO getComprobanteConDetallesById(Long idComprobante) {
        // Obtener comprobante
        Comprobante comprobante = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new EntityNotFoundException("Comprobante de ID " + idComprobante + " no encontrado"));

        // Obtener los detalles del comprobante
        List<DetalleComprobante> detallesOriginales = detalleComprobanteRepository.findDetalleComprobantesByIdComprobante(idComprobante);
        List<DetalleComprobanteConClienteDTO> detalles = new ArrayList<>();
        for (DetalleComprobante detalleActual : detallesOriginales) {
            ClienteDTO integranteObjeto = restTemplate.getForObject(
                    "http://cliente-desc-frecu-service/api/cliente-service/cliente/" + detalleActual.getIdCliente(),
                    ClienteDTO.class);
            detalles.add(new DetalleComprobanteConClienteDTO(detalleActual,integranteObjeto));
        }

        // Obtener reserva segun la id
        // En caso de NotFound Reserva, se lanza mensaje en la peticion
        Reserva reserva = reservaService.getReservaById(comprobante.getIdReserva());

        // Crear objeto Reserva DTO
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setId(reserva.getId());
        reservaDTO.setFecha(reserva.getFecha());
        reservaDTO.setHoraInicio(reserva.getHoraInicio());
        reservaDTO.setHoraFin(reserva.getHoraFin());
        reservaDTO.setEstado(reserva.getEstado());
        reservaDTO.setTotalPersonas(reserva.getTotalPersonas());
        // Obtener objetos cliente y plan mediante peticion http
        ClienteDTO cliente = restTemplate.getForObject("http://cliente-desc-frecu-service/api/cliente-service/cliente/" + reserva.getIdReservante(), ClienteDTO.class);
        PlanDTO plan = restTemplate.getForObject("http://plan-service/api/plan/planes/" + reserva.getIdPlan(), PlanDTO.class);
        reservaDTO.setPlan(plan);
        reservaDTO.setReservante(cliente);



        // Crear objeto combinado de comprobante y detalles
        ComprobanteConDetallesDTO comprobanteConDetalles = new ComprobanteConDetallesDTO();
        comprobanteConDetalles.setId(comprobante.getId());
        comprobanteConDetalles.setTotal(comprobante.getTotal());
        comprobanteConDetalles.setPagado(comprobante.isPagado());
        comprobanteConDetalles.setReserva(reservaDTO);
        comprobanteConDetalles.setDetalles(detalles);

        return comprobanteConDetalles;
    }


    // GET
    // Obtener comprobante con detalles segun id reserva
    public ComprobanteConDetallesDTO getComprobanteConDetallesByIdReserva(Long idReserva) {
        // Obtener comprobante
        Comprobante comprobante = comprobanteRepository.findByIdReserva(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Comprobante no encontrado segun la reserva ID: " + idReserva));

        // Obtener los detalles del comprobante
        List<DetalleComprobante> detallesOriginales = detalleComprobanteRepository
                .findDetalleComprobantesByIdComprobante(comprobante.getId());

        List<DetalleComprobanteConClienteDTO> detalles = new ArrayList<>();
        for (DetalleComprobante detalleActual : detallesOriginales) {
            ClienteDTO integranteObjeto = restTemplate.getForObject(
                    "http://cliente-desc-frecu-service/api/cliente-service/cliente/" + detalleActual.getIdCliente(),
                    ClienteDTO.class);
            detalles.add(new DetalleComprobanteConClienteDTO(detalleActual,integranteObjeto));
        }

        // Obtener reserva segun la id
        Reserva reserva = reservaService.getReservaById(idReserva);

        // Crear objeto Reserva DTO
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setId(reserva.getId());
        reservaDTO.setFecha(reserva.getFecha());
        reservaDTO.setHoraInicio(reserva.getHoraInicio());
        reservaDTO.setHoraFin(reserva.getHoraFin());
        reservaDTO.setEstado(reserva.getEstado());
        reservaDTO.setTotalPersonas(reserva.getTotalPersonas());
        // Obtener objetos cliente y plan mediante peticion http
        ClienteDTO cliente = restTemplate.getForObject("http://cliente-desc-frecu-service/api/cliente-service/cliente/" + reserva.getIdReservante(), ClienteDTO.class);
        PlanDTO plan = restTemplate.getForObject("http://plan-service/api/plan/planes/" + reserva.getIdPlan(), PlanDTO.class);
        reservaDTO.setPlan(plan);
        reservaDTO.setReservante(cliente);



        // Crear objeto combinado de comprobante y detalles
        ComprobanteConDetallesDTO comprobanteConDetalles = new ComprobanteConDetallesDTO();
        comprobanteConDetalles.setId(comprobante.getId());
        comprobanteConDetalles.setTotal(comprobante.getTotal());
        comprobanteConDetalles.setPagado(comprobante.isPagado());
        comprobanteConDetalles.setReserva(reservaDTO);
        comprobanteConDetalles.setDetalles(detalles);

        return comprobanteConDetalles;
    }


    // Create

    // Crear comprobante completo con generacion automatica de detalles por cliente
    public ComprobanteConDetallesDTO createComprobante(Long reservaId, double descuentoExtra) {
        // Condicion no crear en caso de que ya exista comprobante con asociado a la reserva
        if (!comprobanteRepository.findByIdReserva(reservaId).isEmpty()) {
            throw new IllegalArgumentException("Ya existe un comprobante asociado a esta reserva");
        }

        // Condicion base si el descuento es negativo
        if (descuentoExtra < 0) throw new IllegalArgumentException();

        // Obtener reserva segun la id
        Reserva reserva = reservaService.getReservaById(reservaId);

        // Obtener precio de tarifa de la reserva segun el tipo de dia
        double tarifaBase = calcularTarifaBase(reserva.getFecha(), reserva.getIdPlan());
        // Obtener la cantidad de personas del grupo
        int totalPersonas = reserva.getTotalPersonas();

        // Obtener tarifa para cada integrante
        double tarifaIntegrante = tarifaBase / totalPersonas;  // Casteo
        // Obtner descuento extra para cada integrante
        double descuenteExtraIntegrante = descuentoExtra / totalPersonas;

        // Inicializacion de var para contar cumpleañeros. Regla de negocio, cantidad max de descuento por grupo
        int cantidadCumpleanieros = 0;
        // Lista con relaciones id Clientes con la reserva actual
        List<ClienteReserva> integrantes = clienteReservaService.obtenerIntegrantesByIdReserva(reservaId);

        // Caso en que la lista en que la reserva no tiene integrantes asociados. No se pueden crear detalles ni comprobante
        if (integrantes.isEmpty()) throw new IllegalStateException("No hay clientes asociados a la reserva");

        // Caso en que no estan todos los integrantes asignados a la reserva. No se pueden crear detalles ni comprobante
        if (integrantes.size() != totalPersonas) throw new IllegalStateException("No estan todos los clientes asociados a la reserva");



        // Crear objeto comprobante y asignar reserva y estado de no pagado
        Comprobante comprobante = new Comprobante();
        comprobante.setIdReserva(reservaId);
        comprobante.setPagado(true);
        comprobanteRepository.save(comprobante);

        List<DetalleComprobanteConClienteDTO> detalles = new ArrayList<>();
        // Crear detalles para cada persona en la reserva. Iteramos sobre la lista de integrantes
        for (ClienteReserva clienteActual : integrantes) {

            DetalleComprobante detalle = createDetalleComprobante(
                    comprobante.getId(),
                    clienteActual.getIdCliente(),
                    tarifaIntegrante,
                    descuenteExtraIntegrante,
                    totalPersonas,
                    reserva.getFecha(),
                    cantidadCumpleanieros);

            detalleComprobanteRepository.save(detalle);
            // Crear objeto DetalleComprobanteConClienteDTO para estructura de json
            ClienteDTO integranteObjeto = restTemplate.getForObject(
                    "http://cliente-desc-frecu-service/api/cliente-service/cliente/" + clienteActual.getIdCliente(),
                    ClienteDTO.class);
            detalles.add(new DetalleComprobanteConClienteDTO(detalle, integranteObjeto));

            // Si es cumpleaniero, se suma a la cantidad en la variable
            if (esCumpleaniero(clienteActual.getIdCliente(), reserva.getFecha())) cantidadCumpleanieros += 1;

        }

        // Actualizacion del total del comprobante, considerando que ya estan creados los detalles
        actualizarTotalComprobante(comprobante);

        PlanDTO plan = restTemplate.getForObject("http://plan-service/api/plan/planes/" + reserva.getIdPlan(), PlanDTO.class);

        // Logica para poblar tabla de reportes

        // Reportes segun cantidad integrantes
        // Creacion o actualizacion de registro en tabla reporte segun cantidad grupos
        String rangoPersonas = determinarRangoPersonas(reserva.getTotalPersonas());
        // Peticion HTTP POST, notificar al servicio de reportes MC7 sobre el total del comprobante
        notificarReporteIngresosCantidadIntegrantesPeticionPOST(
                rangoPersonas,
                reserva.getFecha(),
                comprobante.getTotal(),
                true
        );

        // Reportes segun planes
        notificarReporteIngresosPlan(
                plan.getId(),
                plan.getDescripcion(),
                reserva.getFecha(),
                comprobante.getTotal(),
                true
        );



        // Crear objeto Reserva DTO
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setId(reserva.getId());
        reservaDTO.setFecha(reserva.getFecha());
        reservaDTO.setHoraInicio(reserva.getHoraInicio());
        reservaDTO.setHoraFin(reserva.getHoraFin());
        reservaDTO.setEstado(reserva.getEstado());
        reservaDTO.setTotalPersonas(reserva.getTotalPersonas());
        // Obtener objetos cliente mediante peticion http
        ClienteDTO cliente = restTemplate.getForObject("http://cliente-desc-frecu-service/api/cliente-service/cliente/" + reserva.getIdReservante(), ClienteDTO.class);
        reservaDTO.setPlan(plan);
        reservaDTO.setReservante(cliente);


        ComprobanteConDetallesDTO comprobanteConDetallesDTO = new ComprobanteConDetallesDTO();
        comprobanteConDetallesDTO.setId(comprobante.getId());
        comprobanteConDetallesDTO.setTotal(comprobante.getTotal());
        comprobanteConDetallesDTO.setPagado(comprobante.isPagado());
        comprobanteConDetallesDTO.setReserva(reservaDTO);
        comprobanteConDetallesDTO.setDetalles(detalles);

        return comprobanteConDetallesDTO;
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

        // Calculo valores para el detalle
        double porcentajeDescuentoEspecial = 0;
        double porcentajeDescuentoGrupo = calcularDescuentoGrupo(totalPersonas);


        // Obtener que tipo de % descuento especial aplica (cliente frecuente o cumpleanios)
        //  Reglas de negocio sobre cantidad maxima de descuentos cumpleaños (50%)
        //   Grupo de 3 a 5: 1 persona max de cumpleanios tiene descuento
        //   Grupo de 6 a 15: 2 personas max de cumpleanios tienen descuento       (Deberia ser hasta 10 personas?)
        //  Cliente frecuente:
        //   No frecuente (0-1): 0%  , Regular (2-4): 10%  , Frecuente (5-6): 20%  , Muy Frecuente (7 o mas): 30%
        if (esCumpleaniero(idCliente, fechaReserva)) {     // Cliente cumpleañero
            // Caso grupo 3 a 5, Cliente cumple años y hay cupo de descuento
            if (totalPersonas>=3 && totalPersonas<=5 && cantidadCumpleanieros<1) {
                porcentajeDescuentoEspecial = calcularDescuentoCumpleanios(idCliente,fechaReserva);
                detalle.setTieneDescuentoCumpleanios(true);

            } else if (totalPersonas>=6 && totalPersonas<=15 && cantidadCumpleanieros <2) {   // Caso grupo de 6 a 15 y hay cupo)
                porcentajeDescuentoEspecial = calcularDescuentoCumpleanios(idCliente, fechaReserva);
                detalle.setTieneDescuentoCumpleanios(true);

            }
        } else {    // Cliento no cumple años. Se verifica si es cliente frecuente
            porcentajeDescuentoEspecial = calcularDescuentoFrecuenteByIdCliente(idCliente);
            if (porcentajeDescuentoEspecial != 0.0) detalle.setTieneDescuentoClienteFrecuente(true);
        }


        // Calculamos el monto con descuentos. Utilizamos descuentos en cascada, es decir, el descuento siguiente
        // se realiza sobre el valor anterior con descuento, no sobre el valor original
        double descuentoGrupo = tarifa * porcentajeDescuentoGrupo;
        double descuentoEspecial = (tarifa - descuentoGrupo) * porcentajeDescuentoEspecial;
        // Calcula el valor total de para un detalle (sin iva)
        double montoConDescuento = tarifa - descuentoGrupo - descuentoEspecial - descuentoExtra;


        // calculo el valor del iva
        double iva = montoConDescuento * IVA;
        // Calculo el total sumando iva
        double total = montoConDescuento + iva;

        // Seteamos valores dentro del detalle, considerando descuento de cumpleanios y especiales
        detalle.setTarifa(tarifa);
        detalle.setDescuentoGrupo(descuentoGrupo);
        detalle.setDescuentoEspecial(descuentoEspecial);
        detalle.setDescuentoExtra(descuentoExtra);
        detalle.setMontoFinal(montoConDescuento);
        detalle.setMontoIva(iva);
        detalle.setMontoTotal(total);

        // Guardaos los porcentajes de descuento en el objeto detalle
        detalle.setPorcentajeDescuentoGrupo(porcentajeDescuentoGrupo*100);
        detalle.setPorcentajeDescuentoEspecial(porcentajeDescuentoEspecial*100);
        return detalle;
    }



    // UPDATE
    // Permite cambiar el estado (BOOL) de un comprobante
    public Comprobante updateEstadoPagadoDeComprobante(Long idComprobante, Boolean pagado) {
        // Obtener comprobante
        Comprobante comprobante = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new EntityNotFoundException("Comprobante no encontrado"));

        comprobante.setPagado(pagado);

        // Obtener reserva y plan
        Reserva reserva = reservaService.getReservaById(comprobante.getIdReserva());
        PlanDTO plan = restTemplate.getForObject("http://plan-service/api/plan/planes/" + reserva.getIdPlan(), PlanDTO.class);

        // Creacion o actualizacion de registro en tabla reporte segun cantidad grupos
        boolean comprobantePagado = false;
        if (comprobante.isPagado()) comprobantePagado = true;

        String rangoPersonas = determinarRangoPersonas(reserva.getTotalPersonas());
        notificarReporteIngresosCantidadIntegrantesPeticionPOST(
                rangoPersonas,
                reserva.getFecha(),
                comprobante.getTotal(),
                comprobantePagado
        );


        // Creacion de registro reportes segun planes
        notificarReporteIngresosPlan(
                plan.getId(),
                plan.getDescripcion(),
                reserva.getFecha(),
                comprobante.getTotal(),
                comprobantePagado
        );

        return comprobanteRepository.save(comprobante);
    }

    // DELETE
    public boolean deleteComprobante(Long idComprobante) {
        // Eliminar detalles de comprobante
        List<DetalleComprobante> detallesOriginales = detalleComprobanteRepository.findDetalleComprobantesByIdComprobante(idComprobante);
        detalleComprobanteRepository.deleteAll(detallesOriginales);

        // Get comprobante, reserva y plan
        Comprobante comprobante = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new EntityNotFoundException("Comprobante no encontrado"));
        Reserva reserva = reservaService.getReservaById(comprobante.getIdReserva());
        PlanDTO plan = restTemplate.getForObject("http://plan-service/api/plan/planes/" + reserva.getIdPlan(), PlanDTO.class);

        // Eliminar comprobante
        comprobanteRepository.deleteById(idComprobante);

        // Peticion http
        // Creacion o actualizacion de registro en tabla reporte segun cantidad grupos
        String rangoPersonas = determinarRangoPersonas(reserva.getTotalPersonas());
        notificarReporteIngresosCantidadIntegrantesPeticionPOST(
                rangoPersonas,
                reserva.getFecha(),
                comprobante.getTotal(),
                false
        );

        // Creacion de registro reportes segun planes
        notificarReporteIngresosPlan(
                plan.getId(),
                plan.getDescripcion(),
                reserva.getFecha(),
                comprobante.getTotal(),
                false
        );

        return true;
    }





    // Metodos privados - logica interna

    // Obtiene porcentaje del descuento para clientes frecuentes
    // Peticion al MC3
    private double calcularDescuentoFrecuenteByIdCliente(Long idCliente) {
        double descuento = restTemplate.getForObject(
                "http://cliente-desc-frecu-service/api/cliente-service/cliente-reserva/porcen-desc-by-visitas-cliente/" + idCliente,
                double.class);
        // considera validacion, si visitas es negativo el descuento es 0.
        return descuento;
    }


    // Obtiene porcentaje de descuento segun el numero de personas del grupo
    // Peticion al MC2
    private double calcularDescuentoGrupo(int totalPersonas) {
        double descuento = restTemplate.getForObject(
                "http://descuento-grupo-service/api/descuento-grupo-service/desc-grupo/cantidad/" + totalPersonas,
                double.class);
        return descuento;
    }


    // Determina porcentaje de descuento de cumpleanios si es que el cliente esta de cumpleanios el un dia de reserva
    // 50% de descuento para cumpleaniero
    private double calcularDescuentoCumpleanios(Long idCliente, LocalDate fecha) {
        boolean esCumpleaniero = restTemplate.getForObject(
                "http://dias-especiales-service/api/dias-especiales-service/cliente-cumpleanios/cliente/" + idCliente +
                        "/esCumpleaniero?fecha=" + fecha,
                boolean.class);
        if (esCumpleaniero) {
            return 0.5;
        } else {
            return 0.0;
        }
    }


    // Obtiene el precio de tarifa del arriendo segun el dia (semana, fin de semana o feriado)
    private double calcularTarifaBase(LocalDate fecha, Long idPlan) {
        // Analisis si es fin de semana o no
        boolean esFinDeSemana = fecha.getDayOfWeek().getValue() >= 6;
        boolean esFeriado = restTemplate.getForObject(
                "http://dias-especiales-service/api/dias-especiales-service/dias-feriados/esFeriado?fecha=" + fecha,
                boolean.class);

        // Obtener Plan, validacion de plan?
        PlanDTO plan = restTemplate.getForObject("http://plan-service/api/plan/planes/" + idPlan, PlanDTO.class);

        // Condicionales, retorno segun tipo de dia
        if (esFeriado) return plan.getPrecioFeriado();
        else if (esFinDeSemana) return plan.getPrecioFinSemana();
        else return plan.getPrecioRegular();
    }


    // Obtiene booleano si es cumpleaniero el cliente
    private boolean esCumpleaniero(Long idCliente, LocalDate fecha) {
        boolean esCumpleaniero = restTemplate.getForObject(
                "http://dias-especiales-service/api/dias-especiales-service/cliente-cumpleanios/cliente/" + idCliente +
                        "/esCumpleaniero?fecha=" + fecha,
                boolean.class);
        return esCumpleaniero;
    }


    // Calcular y actualizar el atributo total de Comprobante segun el los detalles de un comprobante especifico
    @Transactional
    public void actualizarTotalComprobante(Comprobante comprobante) {
        //Comprobante comprobante = getComprobanteById(idComprobante);
        List<DetalleComprobante> detalles = detalleComprobanteRepository
                .findDetalleComprobantesByIdComprobante(comprobante.getId());
        double total = 0.0;
        // Itero en los detalles y voy sumando cada monto total para obtener el total de ganancias del comprobante
        for (DetalleComprobante detalle : detalles) {
            total = total + detalle.getMontoTotal();
        }
        comprobante.setTotal(total);
        comprobanteRepository.save(comprobante);
    }


    // Realizacion de peticion POST para crear o actualizar la tabla de reportes segun grupo en el micro servicio 7
    private void notificarReporteIngresosCantidadIntegrantesPeticionPOST(String rangoPersonas, LocalDate fecha, Double monto, boolean esSuma) {
        String url = "http://reportes-service/api/reportes-service/segun-rango-personas/actualizar" +
                "?rangoPersonas=" + rangoPersonas +
                "&fechaReserva=" + fecha.toString() +
                "&monto=" + monto +
                "&esSuma=" + esSuma;

        restTemplate.postForEntity(url, null, Void.class);
    }

    // Crea string para indicar el rango de personas
    private String determinarRangoPersonas(int cantidad) {
        if (cantidad <= 2) return "1-2 personas";
        if (cantidad <= 5) return "3-5 personas";
        if (cantidad <= 10) return "6-10 personas";
        return "11-15 personas";
    }


    // Realizacion de peticion POST para poblar tabla de reportes segun plan en el microservicio 7
    private void notificarReporteIngresosPlan(Long idPlan, String descripcionPlan, LocalDate fecha, Double monto, boolean esSuma) {
        String url = "http://reportes-service/api/reportes-service/segun-plan/actualizar" +
                "?idPlan=" + idPlan +
                "&descripcionPlan=" + descripcionPlan +
                "&fechaReserva=" + fecha.toString() +
                "&monto=" + monto +
                "&esSuma=" + esSuma;

        restTemplate.postForEntity(url, null, Void.class);
    }

}
