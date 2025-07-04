import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import './CrearReservaModal.css';
import { FaCalendarAlt, FaClock, FaClipboardList, FaUsers, FaCheckCircle, FaUser, FaPlus, FaTrash } from 'react-icons/fa';
import { getPlanes, getPlanesBuscadosByTexto } from '../../services/planService';
import { getClientesActivos, getClientesByNombreParcial } from '../../services/clienteService';
import { createReservaCompleta } from '../../services/reservaService';

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
        if (selectedOption && !integrantesSeleccionados.some(i => i.id === selectedOption.value)) {
            const cliente = clientes.find(c => c.id === selectedOption.value);
            if (cliente) {
                setIntegrantesSeleccionados([...integrantesSeleccionados, cliente]);
                setInputIntegrante('');
            }
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
                    onReservaCreada(); // Actualiza la lista en el padre
                } else {
                    onClose(); // Fallback si onReservaCreada no existe
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
                <button className="cerrar-modal" onClick={onClose}>&times;</button>
                <h2><FaClipboardList /> Crear Nueva Reserva</h2>

                <div className="form-grid">
                    <div className="form-row">
                        <div className="form-group">
                            <label><FaCalendarAlt /> Fecha:</label>
                            <input
                                type="date"
                                value={fecha}
                                onChange={(e) => setFecha(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label><FaClock /> Hora Inicio:</label>
                            <input
                                type="time"
                                value={horaInicio}
                                onChange={(e) => setHoraInicio(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label><FaCheckCircle /> Estado:</label>
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
                        </div>

                        <div className="form-group">
                            <label>Descuento Extra:</label>
                            <input
                                type="number"
                                min="0"
                                value={descuentoExtra}
                                onChange={(e) => setDescuentoExtra(Number(e.target.value))}
                            />
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
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') {
                                handleClienteSearch(e.target.value);
                            }
                        }}
                        placeholder="Buscar o seleccionar cliente..."
                        isSearchable
                        noOptionsMessage={() => "No hay opciones disponibles"}
                        loadingMessage={() => "Buscando..."}
                    />
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
                        />
                    </div>

                    {integrantesSeleccionados.length > 0 && (
                        <div className="tabla-integrantes">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Nombre</th>
                                        <th>Apellido</th>
                                        <th>RUT</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {integrantesSeleccionados.map((integrante) => (
                                        <tr key={integrante.id}>
                                            <td>{integrante.nombre}</td>
                                            <td>{integrante.apellido}</td>
                                            <td>{integrante.rut}</td>
                                            <td>
                                                <button
                                                    className="btn-eliminar"
                                                    onClick={() => eliminarIntegrante(integrante.id)}
                                                >
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
                        <FaCheckCircle /> {isLoading ? 'Creando...' : 'Crear Reserva'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CrearReservaModal;