import './TablaReservas.css';
import { FaEye, FaEdit, FaTrash } from 'react-icons/fa';

const TablaReservas = ({ reservas, onSeleccionar }) => {
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
                                    <button onClick={() => alert('Abrir modal editar')} title="Editar"><FaEdit /></button>
                                    <button onClick={() => alert('Abrir modal eliminar')} title="Eliminar"><FaTrash /></button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default TablaReservas;
