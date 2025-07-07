import { useState } from 'react';
import PropTypes from 'prop-types';
import './TablaReservas.css';
import DeleteReservaModal from './DeleteReservaModal';
import { FaEye, FaEdit, FaTrash, FaCalendarTimes } from 'react-icons/fa';

const TablaReservas = ({ reservas, onSeleccionar, onEditar, refreshReservas, loading }) => {
    const obtenerClaseEstado = (estado) => {
        switch (estado) {
            case 'confirmada':
                return 'estado-confirmada';
            case 'cancelada':
                return 'estado-cancelada';
            case 'completada':
                return 'estado-completada';
            default:
                return '';
        }
    };
    const [reservaToDelete, setReservaToDelete] = useState(null);

    if (loading) return <div className="loading-message">Cargando reservas...</div>

    if (reservas.length === 0) {
        return (
            <div className="sin-resultados-mensaje">
                <FaCalendarTimes className="mensaje-icono" />
                <p>No se encontraron reservas</p>
            </div>
        );
    }

    return (
        <div className="tabla-reservas">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Fecha</th>
                        <th>Horario</th>
                        <th>Estado</th>
                        <th>Cliente</th>
                        <th>Plan</th>
                        <th>Personas</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {reservas.map((res) => (
                        <tr key={res.id}>
                            <td>{res.id}</td>
                            <td>{res.fecha}</td>
                            <td>{res.horaInicio} - {res.horaFin}</td>
                            <td>
                                <span className={`estado ${obtenerClaseEstado(res.estado)}`}>
                                    {res.estado}
                                </span>
                            </td>
                            <td>{res.reservante?.id} - {res.reservante?.nombre} {res.reservante?.apellido}</td>
                            <td>{res.plan?.id} - {res.plan?.descripcion}</td>
                            <td>{res.totalPersonas}</td>
                            <td>
                                <div className="actions-cell">
                                    <div className="tooltip-container">
                                        <button onClick={() => onSeleccionar(res)} className="see-btn">
                                            <FaEye />
                                            <span className="tooltip-text">Ver</span>
                                        </button>
                                    </div>
                                    <div className="tooltip-container">
                                        <button onClick={() => onEditar(res)} className="edit-btn">
                                            <FaEdit />
                                            <span className="tooltip-text">Editar</span>
                                        </button>
                                    </div>
                                    <div className="tooltip-container">
                                        <button
                                            onClick={() => setReservaToDelete(res)}
                                            className="delete-btn"
                                            aria-label="Eliminar plan"
                                        >
                                            <FaTrash />
                                            <span className="tooltip-text">Eliminar</span>
                                        </button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {reservaToDelete && (
                <DeleteReservaModal
                    reserva={reservaToDelete}
                    onClose={() => setReservaToDelete(null)}
                    onSuccess={refreshReservas}
                />
            )}
        </div>
    );
};


TablaReservas.propTypes = {
    reservas: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
            fecha: PropTypes.string.isRequired,
            horaInicio: PropTypes.string.isRequired,
            estado: PropTypes.oneOf(['confirmada', 'cancelada', 'completada']).isRequired,
            totalPersonas: PropTypes.number.isRequired,
            plan: PropTypes.shape({
                id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
                descripcion: PropTypes.string.isRequired
            }).isRequired,
            cliente: PropTypes.shape({
                id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
                nombre: PropTypes.string.isRequired,
                apellido: PropTypes.string.isRequired
            }).isRequired
        })
    ).isRequired,
    onSeleccionar: PropTypes.func.isRequired,
    loading: PropTypes.bool.isRequired,
    onEditar: PropTypes.func.isRequired,
    refreshReservas: PropTypes.func.isRequired
};

export default TablaReservas;