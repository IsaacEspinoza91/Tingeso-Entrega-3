import { useState } from 'react'
import Notification from '../../Notification'
import { FaTimes } from 'react-icons/fa'
import './ConfirmationModal.css'

export default function BaseClienteModal({
    title,
    message,
    onClose,
    actionButton,
    secondaryButton = null,
    notificationProps = {}
}) {
    const [notification, setNotification] = useState({ show: false, message: '', type: '' })

    const showNotification = (message, type) => {
        setNotification({ show: true, message, type })
    }

    const closeNotification = () => {
        setNotification({ ...notification, show: false })
    }

    return (
        <div className="modal-overlay">
            {notification.show && (
                <Notification
                    message={notification.message}
                    type={notification.type}
                    onClose={closeNotification}
                    {...notificationProps}
                />
            )}
            <div className="delete-modal">
                <button className="close-btn" onClick={onClose}>
                    <FaTimes />
                </button>
                <h3>{title}</h3>
                <p>{message}</p>

                <div className="modal-actions">
                    {secondaryButton}
                    {actionButton(showNotification, closeNotification)}
                </div>
            </div>
        </div>
    )
}