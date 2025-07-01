import React, { useState, useEffect } from 'react'
import { createCliente, updateCliente } from '../../services/clienteService'
import { FaSave, FaUserPlus, FaUserEdit, FaTimes, FaIdCard, FaEnvelope, FaPhone, FaCalendarAlt } from 'react-icons/fa'
import Notification from '../Notification'

export default function ClientesForm({ cliente, onClose }) {
    const [formData, setFormData] = useState({
        nombre: '',
        apellido: '',
        rut: '',
        correo: '',
        telefono: '',
        activo: true,
        fechaNacimiento: ''
    })
    const [errors, setErrors] = useState({})
    const [loading, setLoading] = useState(false)
    const [notification, setNotification] = useState({ show: false, message: '', type: '' })

    useEffect(() => {
        if (cliente) {
            setFormData({
                nombre: cliente.nombre,
                apellido: cliente.apellido,
                rut: cliente.rut,
                correo: cliente.correo,
                telefono: cliente.telefono,
                activo: cliente.activo,
                fechaNacimiento: cliente.fechaNacimiento
            })
        }
    }, [cliente])

    const handleChange = (e) => {
        const { name, value } = e.target
        setFormData(prev => ({
            ...prev,
            [name]: value
        }))

        // Clear error when user types
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }))
        }
    }

    const validateForm = () => {
        const newErrors = {}
        let isValid = true

        if (!formData.nombre.trim()) {
            newErrors.nombre = 'El nombre es requerido'
            isValid = false
        }

        if (!formData.apellido.trim()) {
            newErrors.apellido = 'El apellido es requerido'
            isValid = false
        }

        if (!formData.rut.trim()) {
            newErrors.rut = 'El RUT es requerido'
            isValid = false
        }

        if (!formData.correo.trim()) {
            newErrors.correo = 'El correo es requerido'
            isValid = false
        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.correo)) {
            newErrors.correo = 'Ingrese un correo válido'
            isValid = false
        }

        if (!formData.telefono.trim()) {
            newErrors.telefono = 'El teléfono es requerido'
            isValid = false
        }

        if (!formData.fechaNacimiento.trim()) {
            newErrors.telefono = 'La fecha de nacimiento es requerida'
            isValid = false
        }

        setErrors(newErrors)
        return isValid
    }

    const showNotification = (message, type) => {
        setNotification({ show: true, message, type })
    }

    const closeNotification = () => {
        setNotification({ ...notification, show: false })
    }

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (!validateForm()) {
            showNotification('Corrija los errores en el formulario', 'warning')
            return
        }

        setLoading(true)
        try {
            if (cliente) {
                await updateCliente(cliente.id, formData)
                showNotification('Cliente actualizado exitosamente', 'success')
            } else {
                await createCliente(formData)
                showNotification('Cliente creado exitosamente', 'success')
            }

            setTimeout(() => {
                onClose()
            }, 2000)
        } catch (error) {
            console.error('Error:', error)
            showNotification(
                cliente ? 'No se pudo actualizar el cliente' : 'No se pudo crear el cliente',
                'error'
            )
        } finally {
            setLoading(false)
        }
    }

    const renderInput = (name, label, icon, type = 'text') => (
        <div className="form-group">
            <label>
                {icon && React.cloneElement(icon, { className: 'input-icon' })}
                {label}
            </label>
            <input
                type={type}
                name={name}
                value={formData[name] || ''}
                onChange={handleChange}
                className={errors[name] ? 'input-error' : ''}
            />
            {errors[name] && (
                <div className="error-tooltip">
                    {errors[name]}
                </div>
            )}
        </div>
    )

    return (
        <div className="modal-overlay">
            {notification.show && (
                <Notification
                    message={notification.message}
                    type={notification.type}
                    onClose={closeNotification}
                />
            )}
            <div className="clientes-form-modal">
                <button className="close-btn" onClick={onClose}>×</button>
                <h2>{cliente ? 'Editar Cliente' : 'Crear Nuevo Cliente'}</h2>

                <form onSubmit={handleSubmit}>
                    {renderInput('nombre', 'Nombre:', <FaIdCard />)}
                    {renderInput('apellido', 'Apellido:', <FaIdCard />)}
                    {renderInput('rut', 'RUT (Sin puntos y con guión):', <FaIdCard />)}
                    {renderInput('correo', 'Correo:', <FaEnvelope />, 'correo')}
                    {renderInput('telefono', 'Teléfono:', <FaPhone />, 'tel')}
                    {renderInput('fechaNacimiento', 'Fecha de Nacimiento:', <FaCalendarAlt />, 'date')}


                    <div className="form-actions">
                        <button onClick={onClose} className="cancel-btn">
                            <FaTimes className="btn-icon" />
                            Cancelar
                        </button>
                        <button disabled={loading} className="submit-btn">
                            {loading ? (
                                <span>Procesando...</span>
                            ) : cliente ? (
                                <>
                                    <FaSave className="btn-icon" />
                                    <span>Actualizar Cliente</span>
                                </>
                            ) : (
                                <>
                                    <FaUserPlus className="btn-icon" />
                                    <span>Crear Cliente</span>
                                </>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}