import { useEffect } from 'react'
import { FaCheckCircle, FaTimesCircle, FaInfoCircle, FaExclamationTriangle } from 'react-icons/fa'
import './Notification.css'

const icons = {
    success: <FaCheckCircle />,
    error: <FaTimesCircle />,
    info: <FaInfoCircle />,
    warning: <FaExclamationTriangle />
}

export default function Notification({ message, type, onClose }) {
    useEffect(() => {
        const timer = setTimeout(() => {
            onClose()
        }, 5000)
        return () => clearTimeout(timer)
    }, [onClose])

    return (
        <div className={`notification ${type}`}>
            <div className="notification-icon">{icons[type]}</div>
            <div className="notification-message">{message}</div>
            <button onClick={onClose} className="notification-close">
                &times;
            </button>
        </div>
    )
}