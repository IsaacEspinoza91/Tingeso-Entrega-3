import { useState } from 'react'
import { deletePlan } from '../../services/planService'
import { FaTrash, FaTimes } from 'react-icons/fa'

export default function DeletePlanModal({ plan, onClose, onSuccess }) {
    const [loading, setLoading] = useState(false)

    const handleDelete = async () => {
        setLoading(true)
        try {
            await deletePlan(plan.id)
            onSuccess()
            onClose()
        } catch (error) {
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="modal-overlay">
            <div className="delete-modal">
                <button className="close-btn" onClick={onClose}>×</button>
                <h3>Confirmar Eliminación</h3>
                <p>¿Estás seguro que deseas eliminar el plan "{plan.descripcion}" (ID: {plan.id})?</p>

                <div className="modal-actions">
                    <button type="button" onClick={onClose} className="cancel-btn">
                        <FaTimes className="btn-icon" />
                        Cancelar
                    </button>
                    <button
                        onClick={handleDelete}
                        disabled={loading}
                        className="delete-confirm-btn"
                    >
                        <FaTrash className="btn-icon" />
                        {loading ? 'Eliminando...' : 'Eliminar Plan'}
                    </button>
                </div>
            </div>
        </div>
    )
}