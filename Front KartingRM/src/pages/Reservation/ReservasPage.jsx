import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaHome } from 'react-icons/fa';
import {
    getReservas,
    getReservaById,
    getReservasByNombreParcialCliente,
    getReservasByFecha
} from '../../services/reservaService';
import ReservaDetalle from '../../components/reservasv1/ReservaDetalle';
import TablaReservas from '../../components/reservasv1//TablaReservas';
import CrearReservaModal from '../../components/reservasv1/CrearReservaModal';
import { getPlanes } from '../../services/planService';
import { getClientes } from '../../services/clienteService';
import ReservaBusqueda from '../../components/reservasv1/ReservaBusqueda';
import './ReservasPage.css';


const ReservasPage = () => {
    const navigate = useNavigate();
    const [reservas, setReservas] = useState([]);
    const [reservaSeleccionada, setReservaSeleccionada] = useState(null);
    const [mostrarModalCrear, setMostrarModalCrear] = useState(false);
    const [reservaParaEditar, setReservaParaEditar] = useState(null);
    const [planes, setPlanes] = useState([]);
    const [clientes, setClientes] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [searchType, setSearchType] = useState('id');
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        cargarReservas();
    }, []);

    const cargarReservas = async () => {
        setLoading(true)
        try {
            const data = await getReservas();
            setReservas(data);
        } catch (error) {
            console.error('Error cargando reservas:', error);
        } finally {
            setLoading(false)
        }
    };

    const abrirModalCrearReserva = async () => {
        try {
            const [planesData, clientesData] = await Promise.all([getPlanes(), getClientes()]);
            setPlanes(planesData);
            setClientes(clientesData);
            setReservaParaEditar(null); // importante para evitar reuso
            setMostrarModalCrear(true);
        } catch (error) {
            console.error('Error al cargar datos para crear reserva:', error);
        }
    };

    const abrirModalEditarReserva = async (reserva) => {
        try {
            const [planesData, clientesData] = await Promise.all([getPlanes(), getClientes()]);
            setPlanes(planesData);
            setClientes(clientesData);
            setReservaParaEditar(reserva);
            setMostrarModalCrear(true);
        } catch (error) {
            console.error('Error al cargar datos para editar reserva:', error);
        }
    };

    const handleBuscar = async () => {
        if (!searchTerm) return;

        setLoading(true)
        try {

            if (searchType === 'id') {
                const id = parseInt(searchTerm);
                if (!isNaN(id)) {
                    const reserva = await getReservaById(id);
                    setReservas([reserva]);
                }
            } else if (searchType === 'nombre') {
                const resultados = await getReservasByNombreParcialCliente(searchTerm);
                setReservas(resultados);
            } else if (searchType === 'fecha') {
                const resultados = await getReservasByFecha(searchTerm);
                setReservas(resultados);
            }
        } catch (error) {
            console.error('Error en búsqueda de reservas:', error);
            setReservas([]);
        } finally {
            setLoading(false)
        }
    };

    const handleMostrarTodas = async () => {
        setSearchTerm('');
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

                    <ReservaBusqueda
                        searchTerm={searchTerm}
                        setSearchTerm={setSearchTerm}
                        searchType={searchType}
                        setSearchType={setSearchType}
                        onBuscar={handleBuscar}
                        onReset={handleMostrarTodas}
                        onNuevaReserva={abrirModalCrearReserva}
                    />
                </div>

                <TablaReservas
                    reservas={reservas}
                    loading={loading}
                    onSeleccionar={setReservaSeleccionada}
                    onEditar={(reserva) => abrirModalEditarReserva(reserva)}
                    refreshReservas={cargarReservas}
                />




                {reservaSeleccionada && (
                    <ReservaDetalle
                        reserva={reservaSeleccionada}
                        onClose={() => setReservaSeleccionada(null)}
                    />
                )}

                {mostrarModalCrear && (
                    <CrearReservaModal
                        reserva={reservaParaEditar}
                        planes={planes}
                        clientes={clientes}
                        onClose={() => {
                            setMostrarModalCrear(false);
                            setReservaParaEditar(null);
                        }}
                        onReservaCreada={() => {
                            cargarReservas();
                            setMostrarModalCrear(false);
                            setReservaParaEditar(null);
                        }}
                    />
                )}

            </div>
        </div>
    );
};

export default ReservasPage;
