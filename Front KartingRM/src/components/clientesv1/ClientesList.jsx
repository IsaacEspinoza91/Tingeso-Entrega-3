import React from 'react';
import { FaEdit, FaToggleOn, FaToggleOff, FaTrash } from 'react-icons/fa';

export default function ClientesList({
    clientes,
    loading,
    showInactive,
    onEdit,
    onStatusChange,
    onDelete
}) {
    if (loading) return <div className="loading-message">Cargando clientes...</div>;
    if (clientes.length === 0) return <div className="no-results">No se encontraron clientes</div>;

    return (
        <div className="clientes-list-container">
            <table className="clientes-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>RUT</th>
                        <th>Nombre</th>
                        <th>Apellido</th>
                        <th>Correo</th>
                        <th>Teléfono</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {clientes.map((cliente) => (
                        <tr key={cliente.id}>
                            <td>{cliente.id}</td>
                            <td>{cliente.rut}</td>
                            <td>{cliente.nombre}</td>
                            <td>{cliente.apellido}</td>
                            <td>{cliente.correo}</td>
                            <td>{cliente.telefono}</td>
                            <td>
                                <span className={`status-badge ${cliente.activo ? 'active' : 'inactive'}`}>
                                    {cliente.activo ? 'Activo' : 'Inactivo'}
                                </span>
                            </td>
                            <td className="actions-cell">
                                <div className="tooltip-container">
                                    <button onClick={() => onEdit(cliente)} className="edit-btn">
                                        <FaEdit />
                                        <span className="tooltip-text">Editar</span>
                                    </button>
                                </div>
                                <div className="tooltip-container">
                                    <button
                                        onClick={() => onStatusChange(cliente)}
                                        className={`status-btn ${cliente.activo ? 'deactivate' : 'activate'}`}
                                    >
                                        {cliente.activo ? <FaToggleOff /> : <FaToggleOn />}
                                        <span className="tooltip-text">
                                            {cliente.activo ? 'Desactivar' : 'Activar'}
                                        </span>
                                    </button>
                                </div>
                                {!cliente.activo && (
                                    <div className="tooltip-container">
                                        <button
                                            onClick={() => onDelete(cliente.id)}
                                            className="delete-btn"
                                        >
                                            <FaTrash />
                                            <span className="tooltip-text">Eliminar</span>
                                        </button>
                                    </div>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}