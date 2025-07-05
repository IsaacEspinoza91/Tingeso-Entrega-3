import { useState } from 'react'
import { deleteReserva } from '../../services/reservaService'
import { FaTrash, FaTimes } from 'react-icons/fa'
import Notification from '../notificaciones/Notification'

export default function DeleteReservaModal({ reserva, onClose, onSuccess }) {
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
            await deleteReserva(reserva.id)
            showNotification('Reserva eliminado exitosamente', 'success')

            setTimeout(() => {
                onSuccess();
                onClose();
            }, 2000);
        } catch (error) {
            showNotification('No se pudo eliminar el reserva', 'error')
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
                <p>¿Estás seguro que deseas eliminar el reserva  ID: {reserva.id} ?</p>

                <div className="modal-actions">
                    <button
                        onClick={handleDelete}
                        disabled={loading}
                        className="delete-confirm-btn"
                    >
                        <FaTrash className="btn-icon" />
                        {loading ? 'Eliminando...' : 'Eliminar reserva'}
                    </button>
                </div>
            </div>
        </div>
    )
}