import { useState } from 'react'
import { FaUserSlash } from 'react-icons/fa'
import { desactivarCliente } from '../../../services/clienteService'
import BaseClienteModal from './BaseClienteModal'

export default function DesactivarClienteModal({ cliente, onClose, onSuccess }) {
    const [loading, setLoading] = useState(false)

    const handleDesactivar = async (showNotification) => {
        setLoading(true)
        try {
            await desactivarCliente(cliente.id)
            showNotification('Cliente desactivado exitosamente', 'success')
            setTimeout(() => {
                onSuccess()
                onClose()
            }, 2000)
        } catch (error) {
            showNotification('No se pudo desactivar el cliente', 'error')
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    return (
        <BaseClienteModal
            title="Desactivar Cliente"
            message={`¿Estás seguro que deseas desactivar al cliente "${cliente.nombre} ${cliente.apellido}" (RUT: ${cliente.rut})?`}
            onClose={onClose}
            onSuccess={onSuccess}
            actionButton={(showNotification) => (
                <button
                    onClick={() => handleDesactivar(showNotification)}
                    disabled={loading}
                    className="deactivate-btn"
                >
                    <FaUserSlash className="btn-icon" />
                    {loading ? 'Desactivando...' : 'Desactivar Cliente'}
                </button>
            )}
        />
    )
}