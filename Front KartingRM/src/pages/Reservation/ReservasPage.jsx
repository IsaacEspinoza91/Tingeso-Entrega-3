import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaSearch, FaPlus, FaHome, FaListUl } from 'react-icons/fa';
import {
    getReservas,
    getReservaById,
    getReservasByNombreParcialCliente
} from '../../services/reservaService';
import ReservaDetalle from '../../components/reservasv1/ReservaDetalle';
import TablaReservas from '../../components/reservasv1//TablaReservas';
import CrearReservaModal from '../../components/reservasv1/CrearReservaModal';
import { getPlanes } from '../../services/planService';
import { getClientes } from '../../services/clienteService';
import './ReservasPage.css';



const ReservasPage = () => {
    const navigate = useNavigate();
    const [reservas, setReservas] = useState([]);
    const [filtro, setFiltro] = useState('');
    const [tipoBusqueda, setTipoBusqueda] = useState('id');
    const [reservaSeleccionada, setReservaSeleccionada] = useState(null);
    const [mostrarModalCrear, setMostrarModalCrear] = useState(false);
    const [planes, setPlanes] = useState([]);
    const [clientes, setClientes] = useState([]);


    useEffect(() => {
        cargarReservas();
    }, []);

    const cargarReservas = async () => {
        try {
            const data = await getReservas();
            setReservas(data);
        } catch (error) {
            console.error('Error cargando reservas:', error);
        }
    };

    const abrirModalCrearReserva = async () => {
        try {
            const [planesData, clientesData] = await Promise.all([getPlanes(), getClientes()]);
            setPlanes(planesData);
            setClientes(clientesData);
            setMostrarModalCrear(true);
        } catch (error) {
            console.error('Error al cargar datos para crear reserva:', error);
        }
    };


    const handleBuscar = async () => {
        if (!filtro) return;

        try {
            if (tipoBusqueda === 'id') {
                const id = parseInt(filtro);
                if (!isNaN(id)) {
                    const reserva = await getReservaById(id);
                    setReservas([reserva]);
                }
            } else if (tipoBusqueda === 'nombre') {
                const resultados = await getReservasByNombreParcialCliente(filtro);
                setReservas(resultados);
            }
        } catch (error) {
            console.error('Error en búsqueda de reservas:', error);
            setReservas([]);
        }
    };

    const handleMostrarTodas = async () => {
        setFiltro('');
        cargarReservas();
    };

    return (
        <div className="planes-page">
            <div className="planes-container">
                <div className="planes-header">
                    <div className="content-header">
                        <button
                            onClick={() => navigate('/')}
                            className="floating-home-btn"
                            aria-label="Volver al inicio"
                        >
                            <FaHome />
                            <span>Inicio</span>
                        </button>
                        <h1>Gestión de Reservas</h1>
                    </div>

                    <div className="search-container">
                        <div className="search-bar">
                            <div className="search-group">
                                <label htmlFor="reservaSearch" className="search-label">Buscar Reserva:</label>
                                <select
                                    value={tipoBusqueda}
                                    onChange={(e) => setTipoBusqueda(e.target.value)}
                                    className="busqueda-select"
                                >
                                    <option value="id">Por ID</option>
                                    <option value="nombre">Por Nombre</option>
                                </select>
                                <input
                                    id="reservaSearch"
                                    type="text"
                                    placeholder={tipoBusqueda === 'id' ? 'Ingrese ID...' : 'Ingrese nombre del cliente...'}
                                    value={filtro}
                                    onChange={(e) => setFiltro(e.target.value)}
                                />
                                <div className="search-actions">
                                    <button onClick={handleBuscar} className="search-btn">
                                        <FaSearch className="btn-icon" /> Buscar
                                    </button>
                                    {filtro && (
                                        <button onClick={handleMostrarTodas} className="show-all-btn">
                                            <FaListUl className="btn-icon" /> Ver Todos
                                        </button>
                                    )}
                                </div>
                            </div>
                            <button onClick={abrirModalCrearReserva} className="add-btn">
                                <FaPlus className="btn-icon" /> Crear Reserva
                            </button>
                        </div>
                    </div>
                </div>

                <TablaReservas reservas={reservas} onSeleccionar={setReservaSeleccionada} />

                {reservaSeleccionada && (
                    <ReservaDetalle reserva={reservaSeleccionada} onClose={() => setReservaSeleccionada(null)} />
                )}
                {mostrarModalCrear && (
                    <CrearReservaModal
                        planes={planes}
                        clientes={clientes}
                        onClose={() => setMostrarModalCrear(false)}
                        onReservaCreada={() => {
                            cargarReservas();
                            setMostrarModalCrear(false);
                        }}
                    />
                )}

            </div>
        </div>
    );
};

export default ReservasPage;
