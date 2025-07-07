import { useState } from 'react'
import PropTypes from 'prop-types'
import { FaTrash } from 'react-icons/fa'
import { deleteCliente } from '../../../services/clienteService'
import BaseClienteModal from './BaseClienteModal'

export default function EliminarClienteModal({ cliente, onClose, onSuccess }) {
    const [loading, setLoading] = useState(false)

    const handleEliminar = async (showNotification) => {
        setLoading(true)
        try {
            await deleteCliente(cliente.id)
            showNotification('Cliente eliminado permanentemente', 'success')
            setTimeout(() => {
                onSuccess()
                onClose()
            }, 2000)
        } catch (error) {
            showNotification('No se pudo eliminar el cliente', 'error')
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    return (
        <BaseClienteModal
            title="Eliminar Cliente Permanentemente"
            message={`¿Estás seguro que deseas eliminar permanentemente al cliente "${cliente.nombre} ${cliente.apellido}" (RUT: ${cliente.rut})? Esta acción no se puede deshacer.`}
            onClose={onClose}
            onSuccess={onSuccess}
            actionButton={(showNotification) => (
                <button
                    onClick={() => handleEliminar(showNotification)}
                    disabled={loading}
                    className="delete-permanent-btn"
                >
                    <FaTrash className="btn-icon" />
                    {loading ? 'Eliminando...' : 'Eliminar Permanentemente'}
                </button>
            )}
        />
    )
}


EliminarClienteModal.propTypes = {
    cliente: PropTypes.shape({
        id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
        nombre: PropTypes.string.isRequired,
        apellido: PropTypes.string.isRequired,
        rut: PropTypes.string.isRequired
    }).isRequired,
    onClose: PropTypes.func.isRequired,
    onSuccess: PropTypes.func.isRequired
}