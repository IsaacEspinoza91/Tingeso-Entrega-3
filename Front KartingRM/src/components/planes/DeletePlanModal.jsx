import { useState } from 'react'
import { deletePlan } from '../../services/planService'
import { FaTrash } from 'react-icons/fa'
import Notification from '../notificaciones/Notification'

export default function DeletePlanModal({ plan, onClose, onSuccess }) {
    const [loading, setLoading] = useState(false)
    const [notification, setNotification] = useState({ show: false, message: '', type: '' });

    const showNotification = (message, type) => {
        setNotification({ show: true, message, type });
    };

    const closeNotification = () => {
        setNotification({ ...notification, show: false });
    };


    const handleDelete = async () => {
        setLoading(true)
        try {
            await deletePlan(plan.id)
            showNotification('Plan eliminado exitosamente', 'success')

            setTimeout(() => {
                onSuccess();
                onClose();
            }, 2000);
        } catch (error) {
            showNotification('No se pudo eliminar el plan', 'error')
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

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
                <h3>Confirmar Eliminación</h3>
                <p>¿Estás seguro que deseas eliminar el plan "{plan.descripcion}" (ID: {plan.id})?</p>

                <div className="modal-actions">
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