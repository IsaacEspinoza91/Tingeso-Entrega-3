import { useState } from 'react'
import PropTypes from 'prop-types'
import { FaUserCheck } from 'react-icons/fa'
import { activarCliente } from '../../../services/clienteService'
import BaseClienteModal from './BaseClienteModal'

export default function ActivarClienteModal({ cliente, onClose, onSuccess }) {
    const [loading, setLoading] = useState(false)

    const handleActivar = async (showNotification) => {
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

    return (
        <BaseClienteModal
            title="Activar Cliente"
            message={`¿Estás seguro que deseas activar al cliente "${cliente.nombre} ${cliente.apellido}" (RUT: ${cliente.rut})?`}
            onClose={onClose}
            onSuccess={onSuccess}
            actionButton={(showNotification) => (
                <button
                    onClick={() => handleActivar(showNotification)}
                    disabled={loading}
                    className="activate-btn"
                >
                    <FaUserCheck className="btn-icon" />
                    {loading ? 'Activando...' : 'Activar Cliente'}
                </button>
            )}
        />
    )
}

// Def de PropTypes
ActivarClienteModal.propTypes = {
    cliente: PropTypes.shape({
        id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
        nombre: PropTypes.string.isRequired,
        apellido: PropTypes.string.isRequired,
        rut: PropTypes.string.isRequired
    }).isRequired,
    onClose: PropTypes.func.isRequired,
    onSuccess: PropTypes.func.isRequired
}