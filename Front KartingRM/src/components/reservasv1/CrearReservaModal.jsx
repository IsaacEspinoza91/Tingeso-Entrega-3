import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import './CrearReservaModal.css';
import { FaCalendarAlt, FaClock, FaClipboardList, FaUsers, FaCalendarPlus, FaUser, FaUserPlus, FaPlus, FaTrash, FaExclamationCircle, FaTimes } from 'react-icons/fa';
import { getPlanes, getPlanesBuscadosByTexto } from '../../services/planService';
import { getClientesActivos, getClientesByNombreParcial } from '../../services/clienteService';
import { createReservaCompleta } from '../../services/reservaService';
import ClientesForm from '../clientesv1/ClientesForm';

const CrearReservaModal = ({ onClose, onReservaCreada }) => {
    const [fecha, setFecha] = useState('');
    const [horaInicio, setHoraInicio] = useState('');
    const [estado, setEstado] = useState('confirmada');
    const [totalPersonas, setTotalPersonas] = useState(1);
    const [planes, setPlanes] = useState([]);
    const [clientes, setClientes] = useState([]);
    const [planSeleccionado, setPlanSeleccionado] = useState(null);
    const [clienteSeleccionado, setClienteSeleccionado] = useState(null);
    const [integrantesSeleccionados, setIntegrantesSeleccionados] = useState([]);
    const [descuentoExtra, setDescuentoExtra] = useState(0);
    const [inputIntegrante, setInputIntegrante] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [tocadoFecha, setTocadoFecha] = useState(false);
    const [tocadoHora, setTocadoHora] = useState(false);
    const [showNuevoCliente, setShowNuevoCliente] = useState(false);
    const [inputErrors, setInputErrors] = useState({
        horaInicio: '',
        fecha: '',
        descuentoExtra: '',
        totalPersonas: '',
    });


    useEffect(() => {
        const fetchData = async () => {
            try {
                const allPlanes = await getPlanes();
                setPlanes(allPlanes);
                const allClientes = await getClientesActivos();
                setClientes(allClientes);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, []);

    const mostrarTooltip = (campo, mensaje) => {
        setInputErrors(prev => ({
            ...prev,
            [campo]: mensaje
        }));
        setTimeout(() => {
            setInputErrors(prev => ({
                ...prev,
                [campo]: ''
            }));
        }, 3000);
    };

    const handleHoraChange = (e) => {
        const value = e.target.value;
        const [horas] = value.split(':').map(Number);

        if (isNaN(horas)) {
            mostrarTooltip('horaInicio', 'Solo se permiten números positivos');
            return;
        }

        if (horas < 9 || horas > 23) {
            mostrarTooltip('horaInicio', 'Solo se permiten horas entre 9 y 23');
            return;
        }

        setHoraInicio(value);
    };

    const handleFechaChange = (e) => {
        const value = e.target.value;
        const seleccionada = new Date(value);
        const limite = new Date('2025-01-01');

        if (seleccionada < limite) {
            mostrarTooltip('fecha', 'Solo fechas después del año 2025');
            return;
        }

        setFecha(value);
    };

    const handleTotalPersonasChange = (e) => {
        const value = e.target.value;
        if (!/^\d*$/.test(value)) {
            mostrarTooltip('totalPersonas', 'Solo se permiten números positivos');
            return;
        }

        const numero = Number(value);
        setTotalPersonas(numero);
    };

    const handleDescuentoChange = (e) => {
        const value = e.target.value;
        if (!/^\d*$/.test(value)) {
            mostrarTooltip('descuentoExtra', 'Solo se permiten números positivos');
            return;
        }

        setDescuentoExtra(Number(value));
    };





    const getPlanOptions = () => {
        return planes.map((plan) => ({
            value: plan.id,
            label: `${plan.descripcion} (ID: ${plan.id})`
        }));
    };

    const getClienteOptions = () => {
        return clientes.map((cliente) => ({
            value: cliente.id,
            label: `${cliente.nombre} ${cliente.apellido} (ID: ${cliente.id})`
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
            alert('Ya se alcanzó el número máximo de integrantes.');
        }
    };

    const eliminarIntegrante = (id) => {
        setIntegrantesSeleccionados(integrantesSeleccionados.filter(i => i.id !== id));
    };

    const handleCrearReserva = async () => {
        setIsLoading(true);
        try {
            if (!fecha || !horaInicio || !planSeleccionado || !clienteSeleccionado) {
                alert('Complete los campos requeridos');
                return;
            }

            try {
                const response = await createReservaCompleta({
                    fecha,
                    horaInicio,
                    estado,
                    totalPersonas,
                    idPlan: planSeleccionado.value,
                    idReservante: clienteSeleccionado.value,
                    idsIntegrantes: integrantesSeleccionados.map(i => i.id),
                    descuentoExtra: descuentoExtra || 0,
                });

                // Muestra el mensaje de la API si existe
                if (typeof response === 'string') {
                    alert(response); // "Reserva creada exitosamente"
                }

                if (typeof onReservaCreada === 'function') {
                    onReservaCreada();
                } else {
                    onClose();
                }

            } catch (error) {
                console.error('Error:', error);
                alert(error.response?.data || error.message);
            }
        } finally {
            setIsLoading(false);
        }

    };

    return (
        <div className="modal-overlay">
            <div className="crear-reserva-modal">
                <button className="close-btn" onClick={onClose}><FaTimes /></button>
                <h2><FaClipboardList /> Crear Nueva Reserva</h2>

                <div className="form-grid">
                    <div className="form-row">
                        <div className="form-group">
                            <label><FaCalendarAlt /> Fecha:</label>
                            <input
                                type="date"
                                value={fecha}
                                onChange={(e) => setFecha(e.target.value)}
                                onBlur={() => setTocadoFecha(true)}
                                className={tocadoFecha && !fecha ? 'input-error' : ''}
                            />
                            {inputErrors.fecha && (
                                <div className="input-tooltip">{inputErrors.fecha}</div>
                            )}
                        </div>

                        <div className="form-group">
                            <label><FaClock /> Hora Inicio:</label>
                            <input
                                type="time"
                                value={horaInicio}
                                onChange={(e) => setHoraInicio(e.target.value)}
                                onBlur={() => setTocadoHora(true)}
                                className={tocadoHora && !horaInicio ? 'input-error' : ''}
                            />
                            {inputErrors.horaInicio && (
                                <div className="input-tooltip">{inputErrors.horaInicio}</div>
                            )}
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
                                onChange={(e) => setTotalPersonas(Number(e.target.value))}
                            />
                            {inputErrors.totalPersonas && (
                                <div className="input-tooltip">{inputErrors.totalPersonas}</div>
                            )}
                        </div>

                        <div className="form-group">
                            <label>Descuento Extra:</label>
                            <input
                                type="number"
                                min="0"
                                value={descuentoExtra}
                                onChange={(e) => setDescuentoExtra(Number(e.target.value))}
                            />
                            {inputErrors.descuentoExtra && (
                                <div className="input-tooltip">{inputErrors.descuentoExtra}</div>
                            )}
                        </div>
                    </div>
                </div>

                <div className="form-group">
                    <label><FaClipboardList /> Plan:</label>
                    <Select
                        options={getPlanOptions()}
                        value={planSeleccionado}
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
                        options={getClienteOptions()}
                        value={clienteSeleccionado}
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
                        <FaCalendarPlus /> {isLoading ? 'Creando...' : 'Crear Reserva'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CrearReservaModal;