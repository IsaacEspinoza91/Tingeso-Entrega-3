import { useState } from 'react';
import './TablaReservas.css';
import DeleteReservaModal from './DeleteReservaModal'
import { FaEye, FaEdit, FaTrash, FaCalendarTimes } from 'react-icons/fa';

const TablaReservas = ({ reservas, onSeleccionar, onEditar, refreshReservas }) => {
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

    if (reservas.length === 0) {
        return (
            <div className="sin-resultados-mensaje">
                <FaCalendarTimes className="mensaje-icono" />
                <p>No se encontraron reservas con los criterios de búsqueda</p>
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
                                <div className="acciones">
                                    <button onClick={() => onSeleccionar(res)} title="Ver"><FaEye /></button>
                                    <button onClick={() => onEditar(res)} title="Editar"><FaEdit /></button>
                                    <button
                                        onClick={() => setReservaToDelete(res)}
                                        className="delete-btn"
                                        aria-label="Eliminar plan"
                                    >
                                        <FaTrash />
                                        <span className="tooltip-text">Eliminar</span>
                                    </button>
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

export default TablaReservas;
