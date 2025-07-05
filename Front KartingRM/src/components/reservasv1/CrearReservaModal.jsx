import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import './CrearReservaModal.css';
import { FaCalendarAlt, FaClock, FaClipboardList, FaUsers, FaCalendarPlus, FaUser, FaUserPlus, FaExclamationTriangle, FaTrash, FaExclamationCircle, FaCheckCircle, FaTimes, FaMoneyBillWave } from 'react-icons/fa';
import { getPlanes, getPlanesBuscadosByTexto } from '../../services/planService';
import { getClientesActivos, getClientesByNombreParcial } from '../../services/clienteService';
import { createReservaCompleta, updateReservaCompleta, getIntegrantes, getReservaById } from '../../services/reservaService';
import ClientesForm from '../clientesv1/ClientesForm';
import Notification from '../notificaciones/Notification';
import Spinner from '../spinner/Spinner';

const CrearReservaModal = ({ onClose, onReservaCreada, reserva = null }) => {
    const [fecha, setFecha] = useState('');
    const [horaInicio, setHoraInicio] = useState('');
    const [estado, setEstado] = useState('confirmada');
    const [totalPersonas, setTotalPersonas] = useState(1);
    const [planes, setPlanes] = useState([]);
    const [clientes, setClientes] = useState([]);
    const [pagado, setPagado] = useState(false);
    const [planSeleccionado, setPlanSeleccionado] = useState(null);
    const [clienteSeleccionado, setClienteSeleccionado] = useState(null);
    const [integrantesSeleccionados, setIntegrantesSeleccionados] = useState([]);
    const [descuentoExtra, setDescuentoExtra] = useState(0);
    const [inputIntegrante, setInputIntegrante] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [notification, setNotification] = useState({
        id: Date.now(),
        show: false,
        message: '',
        type: ''
    });
    const [showNuevoCliente, setShowNuevoCliente] = useState(false);
    const [inputErrors, setInputErrors] = useState({
        horaInicio: '',
        fecha: '',
        descuentoExtra: '',
        totalPersonas: '',
    });

    const [isFormReady, setIsFormReady] = useState(false);



    useEffect(() => {
        const cargarDatosReserva = async () => {
            if (!reserva) return;

            try {
                const data = await getReservaById(reserva.id);
                const integrantesData = await getIntegrantes(reserva.id);

                setFecha(data.fecha);
                setHoraInicio(data.horaInicio?.slice(0, 5));
                setEstado(data.estado);
                setTotalPersonas(data.totalPersonas);
                setDescuentoExtra(data.descuentoExtra || 0);
                setPagado(data.pagado || false);

                const planMatch = planes.find(p => p.id === data.plan?.id);
                const clienteMatch = clientes.find(c => c.id === data.reservante?.id);

                if (planMatch) {
                    setPlanSeleccionado({
                        value: planMatch.id,
                        label: `${planMatch.id} - ${planMatch.descripcion}`
                    });
                }

                if (clienteMatch) {
                    setClienteSeleccionado({
                        value: clienteMatch.id,
                        label: `${clienteMatch.id} - ${clienteMatch.nombre} ${clienteMatch.apellido}`
                    });
                }

                const integrantesConvertidos = integrantesData.map(cli => ({
                    id: cli.id,
                    nombre: cli.nombre,
                    apellido: cli.apellido,
                    rut: cli.rut
                }));

                setIntegrantesSeleccionados(integrantesConvertidos);
                setIsFormReady(true);

            } catch (error) {
                console.error('Error al cargar datos de reserva:', error);
            }
        };

        cargarDatosReserva();
    }, [reserva, planes, clientes]);



    useEffect(() => {
        if (!reserva) {
            // Reinicia el formulario para creación nueva
            setPlanSeleccionado(null);
            setClienteSeleccionado(null);
            setFecha('');
            setHoraInicio('');
            setEstado('confirmada');
            setTotalPersonas(1);
            setDescuentoExtra(0);
            setPagado(false);
            setIntegrantesSeleccionados([]);
            setIsFormReady(true); // Si es creación nueva, no hay que esperar carga
        }
    }, [reserva]);




    const showNotification = (message, type) => {
        setNotification({
            id: Date.now(), // Nuevo ID cada vez
            show: true,
            message,
            type
        });
    };

    const closeNotification = () => {
        setNotification(prev => ({ ...prev, show: false }));
    };
    const mostrarError = (campo, mensaje) => {
        setInputErrors(prev => ({
            ...prev,
            [campo]: mensaje
        }));
    };

    const limpiarError = (campo) => {
        setInputErrors(prev => ({
            ...prev,
            [campo]: ''
        }));
    };

    const handleHoraChange = (e) => {
        const value = e.target.value;

        if (!/^\d{2}:\d{2}$/.test(value)) {
            mostrarError('horaInicio', 'Solo se permiten números en formato HH:mm');
            return;
        }

        setHoraInicio(value);
        limpiarError('horaInicio');
    };

    const handleHoraBlur = () => {
        if (!horaInicio) return;

        const [hh] = horaInicio.split(':').map(Number);
        if (hh < 9 || hh > 23) {
            mostrarError('horaInicio', 'Hora debe ser entre 09:00 y 23:00');
        } else {
            limpiarError('horaInicio');
        }
    };

    const handleFechaChange = (e) => {
        const value = e.target.value;

        if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) {
            mostrarError('fecha', 'Solo se permiten números en formato DD-MM-AAA');
            return;
        }

        setFecha(value);
        limpiarError('fecha');
    };

    const handleFechaBlur = () => {
        if (!fecha) return;

        const seleccionada = new Date(fecha);
        const limite = new Date('2025-01-01');

        if (seleccionada < limite) {
            mostrarError('fecha', 'Solo fechas desde el 2025 en adelante');
        } else {
            limpiarError('fecha');
        }
    };


    const handleTotalPersonasChange = (e) => {
        const value = e.target.value;
        if (!/^\d*$/.test(value)) {
            mostrarError('totalPersonas', 'Solo se permiten números');
            return;
        }

        setTotalPersonas(Number(value));
        limpiarError('totalPersonas');
    };

    const handleTotalPersonasBlur = () => {
        if (totalPersonas > 15) {
            mostrarError('totalPersonas', 'Máximo permitido: 15 personas');
        } else if (totalPersonas == 0) {
            mostrarError('totalPersonas', 'Mínimimo permitido: 1 persona');
        } else {
            limpiarError('totalPersonas');
        }
    };

    const handleDescuentoChange = (e) => {
        const value = e.target.value;
        if (!/^\d*$/.test(value)) {
            mostrarError('descuentoExtra', 'Solo se permiten números');
            return;
        }

        setDescuentoExtra(Number(value));
        limpiarError('descuentoExtra');
    };


    const getPlanOptions = () => {
        return planes.map(p => ({
            value: p.id,
            label: `${p.id} - ${p.descripcion}`
        }));
    };

    const getClienteOptions = () => {
        return clientes.map(c => ({
            value: c.id,
            label: `${c.id} - ${c.nombre} ${c.apellido}`
        }));
    };

    const getIntegranteOptions = () => {
        return clientes
            .filter(cliente => !integrantesSeleccionados.some(i => i.id === cliente.id))
            .map((cliente) => ({
                value: cliente.id,
                label: `${cliente.nombre} ${cliente.apellido} - ${cliente.rut}`
            }));
    };

    const handlePlanInputChange = (inputValue) => {
        return inputValue;
    };

    const handleClienteInputChange = (inputValue) => {
        return inputValue;
    };

    const handlePlanSearch = async (inputValue) => {
        try {
            if (!inputValue) {
                const all = await getPlanes();
                setPlanes(all);
            } else {
                const buscados = await getPlanesBuscadosByTexto(inputValue);
                setPlanes(buscados);
            }
        } catch (error) {
            console.error('Error searching planes:', error);
        }
    };

    const handleClienteSearch = async (inputValue) => {
        try {
            if (!inputValue) {
                const all = await getClientesActivos();
                setClientes(all);
            } else {
                const buscados = await getClientesByNombreParcial(inputValue);
                setClientes(buscados);
            }
        } catch (error) {
            console.error('Error searching clientes:', error);
        }
    };

    const agregarIntegrante = (selectedOption) => {
        if (
            selectedOption &&
            !integrantesSeleccionados.some(i => i.id === selectedOption.value) &&
            integrantesSeleccionados.length < totalPersonas
        ) {
            const cliente = clientes.find(c => c.id === selectedOption.value);
            if (cliente) {
                setIntegrantesSeleccionados([...integrantesSeleccionados, cliente]);
                setInputIntegrante('');
            }
        } else if (integrantesSeleccionados.length >= totalPersonas) {
            showNotification('Ya se alcanzó el número máximo de integrantes.', 'warning')
        }
    };

    const eliminarIntegrante = (id) => {
        setIntegrantesSeleccionados(integrantesSeleccionados.filter(i => i.id !== id));
    };

    const handleCrearReserva = async () => {
        setIsLoading(true);
        try {
            const errores = [];

            if (!fecha) errores.push("fecha");
            if (!horaInicio) errores.push("hora de inicio");
            if (!planSeleccionado) errores.push("plan");
            if (!clienteSeleccionado) errores.push("cliente reservante");

            if (errores.length > 0) {
                showNotification(
                    `Complete los campos requeridos: ${errores.join(', ')}`,
                    'warning'
                );
                setIsLoading(false);
                return;
            }

            const reservaData = {
                fecha,
                horaInicio,
                estado,
                totalPersonas,
                idPlan: planSeleccionado.value,
                idReservante: clienteSeleccionado.value,
                idsIntegrantes: integrantesSeleccionados.map(i => i.id),
                descuentoExtra: descuentoExtra || 0,
                pagado: reserva ? pagado : undefined,
            };

            let response;
            if (reserva) {
                response = await updateReservaCompleta(reserva.id, reservaData);
                showNotification(response, 'success');
            } else {
                response = await createReservaCompleta(reservaData);
                showNotification(response, 'success');
            }

            // Forzar re-renderizado de la notificación
            setNotification(prev => ({ ...prev, id: Date.now() }));


            await new Promise(resolve => setTimeout(resolve, 500));

            if (typeof onReservaCreada === 'function') {
                await onReservaCreada();
            }

            setTimeout(() => {
                onClose();
            }, 2000);
        } catch (error) {
            console.error('Error:', error);
            showNotification(
                error.response?.data ||
                error.message ||
                'Error al procesar la reserva',
                'error'
            );
        } finally {
            setIsLoading(false);
        }
    };

    if (reserva && !isFormReady) {
        return <Spinner mensaje="Cargando datos de la reserva..." />;
    }

    return (
        <div className="modal-overlay">
            {notification.show && (
                <Notification
                    key={notification.id}
                    message={notification.message}
                    type={notification.type}
                    onClose={closeNotification}
                />
            )}
            <div className="crear-reserva-modal">
                <button className="close-btn" onClick={onClose}><FaTimes /></button>
                <h2><FaClipboardList /> {reserva ? 'Editar Reserva' : 'Crear Nueva Reserva'}</h2>

                <div className="form-grid">
                    <div className="form-row">
                        <div className="form-group">
                            <label><FaCalendarAlt /> Fecha:</label>
                            <input
                                type="date"
                                value={fecha}
                                onChange={handleFechaChange}
                                onBlur={handleFechaBlur}
                            />
                            {inputErrors.fecha && <div className="error-tooltip"><FaExclamationTriangle className="error-icon" />{inputErrors.fecha}</div>}
                        </div>

                        <div className="form-group">
                            <label><FaClock /> Hora Inicio:</label>
                            <input
                                type="time"
                                value={horaInicio}
                                onChange={handleHoraChange}
                                onBlur={handleHoraBlur}
                            />
                            {inputErrors.horaInicio && <div className="error-tooltip"><FaExclamationTriangle className="error-icon" />{inputErrors.horaInicio}</div>}
                        </div>

                        <div className="form-group">
                            <label><FaExclamationCircle /> Estado:</label>
                            <select
                                value={estado}
                                onChange={(e) => setEstado(e.target.value)}
                            >
                                <option value="confirmada">Confirmada</option>
                                <option value="completada">Completada</option>
                                <option value="cancelada">Cancelada</option>
                            </select>
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label><FaUsers /> Total Personas:</label>
                            <input
                                type="number"
                                min="1"
                                max="15"
                                value={totalPersonas}
                                onChange={handleTotalPersonasChange}
                                onBlur={handleTotalPersonasBlur}
                            />
                            {inputErrors.totalPersonas && <div className="error-tooltip"><FaExclamationTriangle className="error-icon" />{inputErrors.totalPersonas}</div>}
                        </div>

                        <div className="form-group">
                            <label><FaMoneyBillWave /> Descuento Extra:</label>
                            <input
                                type="number"
                                min="0"
                                value={descuentoExtra}
                                onChange={handleDescuentoChange}
                            />
                            {inputErrors.descuentoExtra && <div className="error-tooltip"><FaExclamationTriangle className="error-icon" />{inputErrors.descuentoExtra}</div>}
                        </div>

                        {reserva && (
                            <div className="form-group form-checkbox">
                                <label htmlFor="pagadoCheckbox">¿Pagado?</label>
                                <input
                                    id="pagadoCheckbox"
                                    type="checkbox"
                                    checked={pagado}
                                    onChange={(e) => setPagado(e.target.checked)}
                                />
                            </div>
                        )}
                    </div>
                </div>
                {reserva && (
                    <h5>Nota: Antes de editar, debe seleccionar los elementos de plan y cliente. Automáticamente se selecionarán los previos en la reserva.</h5>
                )}
                <div className="form-group">
                    <label><FaClipboardList /> Plan:</label>
                    <Select
                        key={planSeleccionado?.value || 'plan-select'}
                        options={getPlanOptions()}
                        value={getPlanOptions().find(opt => opt.value === planSeleccionado?.value) || null}
                        onChange={setPlanSeleccionado}
                        onInputChange={handlePlanInputChange}
                        onMenuOpen={() => handlePlanSearch('')}
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') {
                                handlePlanSearch(e.target.value);
                            }
                        }}
                        placeholder="Buscar o seleccionar plan..."
                        isSearchable
                        noOptionsMessage={() => "No hay opciones disponibles"}
                        loadingMessage={() => "Buscando..."}
                        classNamePrefix="Select"
                        menuPortalTarget={document.body}
                        styles={{
                            menuPortal: (base) => ({ ...base, zIndex: 1200 })
                        }}
                    />
                </div>

                <div className="form-group">
                    <label><FaUser /> Cliente Reservante:</label>
                    <Select
                        key={clienteSeleccionado?.value || 'cliente-select'}
                        options={getClienteOptions()}
                        value={getClienteOptions().find(opt => opt.value === clienteSeleccionado?.value) || null}
                        onChange={setClienteSeleccionado}
                        onInputChange={handleClienteInputChange}
                        onMenuOpen={() => handleClienteSearch('')}
                        placeholder="Buscar o seleccionar cliente..."
                        isSearchable
                        noOptionsMessage={() => "No hay opciones disponibles"}
                        menuPortalTarget={document.body}
                        styles={{
                            menuPortal: base => ({ ...base, zIndex: 1200 })
                        }}
                        classNamePrefix="Select"
                    />
                    <button className="boton-nuevo-cliente" onClick={() => setShowNuevoCliente(true)}>
                        <FaUserPlus /> Nuevo Cliente
                    </button>
                    {showNuevoCliente && (
                        <div style={{ marginTop: '2rem' }}>
                            <ClientesForm
                                modoCompacto={true}
                                onClose={() => setShowNuevoCliente(false)}
                                onClienteCreado={(nuevoCliente) => {
                                    setShowNuevoCliente(false);

                                    setClientes((prevClientes) => {
                                        const yaExiste = prevClientes.some(c => c.id === nuevoCliente.id);
                                        if (!yaExiste) {
                                            return [...prevClientes, nuevoCliente];
                                        }
                                        return prevClientes;
                                    });

                                    setClienteSeleccionado({
                                        value: nuevoCliente.id,
                                        label: `${nuevoCliente.nombre} ${nuevoCliente.apellido} (ID: ${nuevoCliente.id})`
                                    });
                                }}

                            />
                        </div>
                    )}
                </div>

                <div className="form-group">
                    <label><FaUsers /> Integrantes:</label>
                    <div className="agregar-integrante">
                        <Select
                            options={getIntegranteOptions()}
                            value={null}
                            onChange={agregarIntegrante}
                            inputValue={inputIntegrante}
                            onInputChange={setInputIntegrante}
                            placeholder="Buscar y agregar integrante..."
                            isSearchable
                            noOptionsMessage={() => "No hay opciones disponibles"}
                            isDisabled={integrantesSeleccionados.length >= totalPersonas}
                            classNamePrefix="Select"
                            menuPortalTarget={document.body}
                            styles={{
                                menuPortal: (base) => ({ ...base, zIndex: 1200 })
                            }}
                        />
                    </div>

                    {integrantesSeleccionados.length > 0 && (
                        <div className="tabla-integrantes">
                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nombre Completo</th>
                                        <th>RUT</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {integrantesSeleccionados.map((integrante) => (
                                        <tr key={integrante.id}>
                                            <td>{integrante.id}</td>
                                            <td>{`${integrante.nombre} ${integrante.apellido}`}</td>
                                            <td>{integrante.rut}</td>
                                            <td>
                                                <button className="btn-eliminar" onClick={() => eliminarIntegrante(integrante.id)}>
                                                    <FaTrash />
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>

                <div className="form-actions">
                    <button
                        className="btn-crear"
                        onClick={handleCrearReserva}
                        disabled={isLoading}
                    >
                        <FaCheckCircle />
                        {isLoading ? 'Procesando...' : reserva ? 'Actualizar Reserva' : 'Crear Reserva'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CrearReservaModal;