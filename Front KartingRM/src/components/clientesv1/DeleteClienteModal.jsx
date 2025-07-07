import { useState } from 'react'
import PropTypes from 'prop-types'
import { deleteCliente, desactivarCliente, activarCliente } from '../../services/clienteService'
import { FaTrash, FaUserCheck } from 'react-icons/fa'
import Notification from '../Notification'

export default function DeleteClienteModal({ cliente, onClose, onSuccess }) {
    const [loading, setLoading] = useState(false)
    const [notification, setNotification] = useState({ show: false, message: '', type: '' })

    const showNotification = (message, type) => {
        setNotification({ show: true, message, type })
    }

    const closeNotification = () => {
        setNotification({ ...notification, show: false })
    }

    const handleDelete = async () => {
        setLoading(true)
        try {
            if (cliente.activo) {
                await desactivarCliente(cliente.id)
                showNotification('Cliente desactivado exitosamente', 'success')
            } else {
                await deleteCliente(cliente.id)
                showNotification('Cliente eliminado permanentemente', 'success')
            }

            setTimeout(() => {
                onSuccess()
                onClose()
            }, 2000)
        } catch (error) {
            showNotification(
                cliente.activo
                    ? 'No se pudo desactivar el cliente'
                    : 'No se pudo eliminar el cliente',
                'error'
            )
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    const handleActivate = async () => {
        setLoading(true)
        try {
            await activarCliente(cliente.id)
            showNotification('Cliente activado exitosamente', 'success')

            setTimeout(() => {
                onSuccess()
                onClose()
            }, 2000)
        } catch (error) {
            showNotification('No se pudo activar el cliente', 'error')
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    const textoBotonEliminar = loading
        ? cliente.activo
            ? 'Desactivando...'
            : 'Eliminando...'
        : cliente.activo
            ? 'Desactivar'
            : 'Eliminar Permanentemente';


    return (
        <div className="modal-overlay">
            {notification.show && (
                <Notification
                    message={notification.message}
                    type={notification.type}
                    onClose={closeNotification}
                />
            )}
            <div className="delete-modal">
                <button className="close-btn" onClick={onClose}>×</button>
                <h3>{cliente.activo ? 'Desactivar Cliente' : 'Eliminar Cliente'}</h3>
                <p>
                    ¿Estás seguro que deseas {cliente.activo ? 'desactivar' : 'eliminar permanentemente'} al cliente
                    "{cliente.nombre} {cliente.apellido}" (RUT: {cliente.rut})?
                </p>

                <div className="modal-actions">
                    {!cliente.activo && (
                        <button
                            onClick={handleActivate}
                            disabled={loading}
                            className="activate-btn"
                        >
                            <FaUserCheck className="btn-icon" />
                            {loading ? 'Activando...' : 'Activar Cliente'}
                        </button>
                    )}
                    <button
                        onClick={handleDelete}
                        disabled={loading}
                        className="delete-confirm-btn"
                    >
                        <FaTrash className="btn-icon" />
                        {textoBotonEliminar}
                    </button>
                </div>
            </div>
        </div>
    )
}


DeleteClienteModal.propTypes = {
    cliente: PropTypes.shape({
        id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
        nombre: PropTypes.string.isRequired,
        apellido: PropTypes.string.isRequired,
        rut: PropTypes.string.isRequired,
        activo: PropTypes.bool.isRequired
    }).isRequired,
    onClose: PropTypes.func.isRequired,
    onSuccess: PropTypes.func.isRequired
}