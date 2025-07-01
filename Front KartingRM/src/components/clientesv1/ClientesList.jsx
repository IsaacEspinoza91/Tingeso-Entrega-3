import { useState } from 'react'
import { FaEdit, FaTrash, FaUserCheck, FaUserSlash } from 'react-icons/fa'
import DesactivarClienteModal from './confirmationModal/DesactivarClienteModal'
import EliminarClienteModal from './confirmationModal/EliminarClienteModal'
import ActivarClienteModal from './confirmationModal/ActivarClienteModal'

export default function ClientesList({
    clientes,
    loading,
    onEdit,
    refreshClientes
}) {
    const [modalType, setModalType] = useState(null)
    const [selectedCliente, setSelectedCliente] = useState(null)

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleDateString('es-CL');
    };

    const handleCloseModal = () => {
        setModalType(null)
        setSelectedCliente(null)
    }

    if (loading) return <div className="loading-message">Cargando clientes...</div>
    if (clientes.length === 0) return <div className="no-results">No se encontraron clientes</div>

    return (
        <div className="clientes-list-container">
            <table className="clientes-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>RUT</th>
                        <th>Correo</th>
                        <th>Teléfono</th>
                        <th>Fecha Nacimiento</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {clientes.map((cliente) => (
                        <tr key={cliente.id}>
                            <td>{cliente.id}</td>
                            <td>{cliente.nombre} {cliente.apellido}</td>
                            <td>{cliente.rut}</td>
                            <td>{cliente.correo}</td>
                            <td>{cliente.telefono}</td>
                            <td>{formatDate(cliente.fechaNacimiento)}</td>
                            <td className={cliente.activo ? 'status-active' : 'status-inactive'}>
                                {cliente.activo ? 'Activo' : 'Inactivo'}
                            </td>
                            <td className="actions-cell">
                                <div className="tooltip-container">
                                    <button
                                        onClick={() => onEdit(cliente)}
                                        className="edit-btn"
                                        aria-label="Editar cliente"
                                    >
                                        <FaEdit />
                                        <span className="tooltip-text">Editar</span>
                                    </button>
                                </div>

                                {cliente.activo ? (
                                    <div className="tooltip-container">
                                        <button
                                            onClick={() => {
                                                setSelectedCliente(cliente)
                                                setModalType('desactivar')
                                            }}
                                            className="deactivate-btn"
                                            aria-label="Desactivar cliente"
                                        >
                                            <FaUserSlash />
                                            <span className="tooltip-text">Desactivar</span>
                                        </button>
                                    </div>
                                ) : (
                                    <>
                                        <div className="tooltip-container">
                                            <button
                                                onClick={() => {
                                                    setSelectedCliente(cliente)
                                                    setModalType('eliminar')
                                                }}
                                                className="delete-btn"
                                                aria-label="Eliminar cliente"
                                            >
                                                <FaTrash />
                                                <span className="tooltip-text">Eliminar</span>
                                            </button>
                                        </div>
                                        <div className="tooltip-container">
                                            <button
                                                onClick={() => {
                                                    setSelectedCliente(cliente)
                                                    setModalType('activar')
                                                }}
                                                className="activate-btn"
                                                aria-label="Activar cliente"
                                            >
                                                <FaUserCheck />
                                                <span className="tooltip-text">Activar</span>
                                            </button>
                                        </div>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Modales */}
            {modalType === 'desactivar' && (
                <DesactivarClienteModal
                    cliente={selectedCliente}
                    onClose={handleCloseModal}
                    onSuccess={refreshClientes}
                />
            )}

            {modalType === 'eliminar' && (
                <EliminarClienteModal
                    cliente={selectedCliente}
                    onClose={handleCloseModal}
                    onSuccess={refreshClientes}
                />
            )}

            {modalType === 'activar' && (
                <ActivarClienteModal
                    cliente={selectedCliente}
                    onClose={handleCloseModal}
                    onSuccess={refreshClientes}
                />
            )}
        </div>
    )
}