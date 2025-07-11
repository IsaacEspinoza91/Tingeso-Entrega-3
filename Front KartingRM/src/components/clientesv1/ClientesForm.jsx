import React, { useState, useEffect } from 'react'
import PropTypes from 'prop-types'
import { createCliente, updateCliente } from '../../services/clienteService'
import { FaSave, FaUserPlus, FaTimes, FaIdCard, FaEnvelope, FaPhone, FaCalendarAlt, FaExclamationTriangle } from 'react-icons/fa'
import Notification from '../notificaciones/Notification'

const ClientesForm = ({ cliente, onClose, modoCompacto = false }) => {
    const [formData, setFormData] = useState({
        nombre: '',
        apellido: '',
        rut: '',
        correo: '',
        telefono: '+569', // Valor inicial con +569
        activo: true,
        fechaNacimiento: ''
    });
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    const [notification, setNotification] = useState({ show: false, message: '', type: '' });

    useEffect(() => {
        if (cliente) {
            // Manejo de fecha de nacimiento
            let fechaNacimientoValue = '';
            if (cliente.fechaNacimiento) {
                // Crear fecha sin ajustar zona horaria (tratar como UTC)
                const date = new Date(cliente.fechaNacimiento);
                fechaNacimientoValue = date.toISOString().split('T')[0];
            }

            setFormData({
                nombre: cliente.nombre,
                apellido: cliente.apellido,
                rut: cliente.rut,
                correo: cliente.correo,
                telefono: cliente.telefono.startsWith('+569') ? cliente.telefono : '+569' + cliente.telefono.replace(/\D/g, '').slice(0, 8),
                activo: cliente.activo,
                fechaNacimiento: fechaNacimientoValue
            });
        }
    }, [cliente]);

    const handleTelefonoChange = (e) => {
        const value = e.target.value;

        // Mantener el +569 fijo
        if (value.length < 4) {
            setFormData(prev => ({
                ...prev,
                telefono: '+569'
            }));
            return;
        }

        // Solo permitir números después del +569
        const numeros = value.slice(4).replace(/\D/g, '');
        const telefonoCompleto = '+569' + numeros.slice(0, 8); // +569 + 8 dígitos

        setFormData(prev => ({
            ...prev,
            telefono: telefonoCompleto
        }));

        // Validación en tiempo real solo para caracteres no numéricos
        if (/\D/.test(value.slice(4))) {
            setErrors(prev => ({
                ...prev,
                telefono: 'Solo se permiten números'
            }));
        } else {
            setErrors(prev => ({
                ...prev,
                telefono: ''
            }));
        }
    };

    const handleBlur = (e) => {
        const { name, value } = e.target

        // Validación específica al perder foco
        switch (name) {
            case 'correo':
                if (value && !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
                    setErrors(prev => ({ ...prev, correo: 'Formato válido: usuario@dominio.cl' }))
                } else {
                    setErrors(prev => ({ ...prev, correo: '' }))
                }
                break

            case 'fechaNacimiento':
                if (value) {
                    const today = new Date()
                    const birthDate = new Date(value)
                    const age = today.getFullYear() - birthDate.getFullYear()

                    if (age < 8 || age > 120) {
                        setErrors(prev => ({ ...prev, fechaNacimiento: 'Edad debe ser entre 8 y 120 años' }))
                    } else {
                        setErrors(prev => ({ ...prev, fechaNacimiento: '' }))
                    }
                }
                break

            case 'rut':
                if (value && !/^\d{7,8}-[\dkK]$/.test(value)) {
                    setErrors(prev => ({ ...prev, rut: 'Formato: 12345678-9' }))
                } else {
                    setErrors(prev => ({ ...prev, rut: '' }))
                }
                break

            case 'telefono':
                if (value.length < 12) {
                    setErrors(prev => ({
                        ...prev,
                        telefono: 'Formato completo: +569XXXXXXXX'
                    }));
                } else {
                    setErrors(prev => ({
                        ...prev,
                        telefono: ''
                    }));
                }
                break

        }
    }

    const handleChange = (e) => {
        const { name, value } = e.target
        let validatedValue = value
        let error = ''

        // Validación en tiempo real
        switch (name) {
            case 'nombre':
            case 'apellido':
                // Solo permite letras y espacios
                if (/[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s'-]/.test(value)) {
                    error = 'Solo se permiten letras'
                }
                validatedValue = value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s'-]/g, '')
                break

            case 'rut':
                // Validación en tiempo real para caracteres permitidos
                if (/[^\dkK-]/.test(value)) {
                    error = 'Solo se permiten números, - y K'
                }
                validatedValue = value.replace(/[^\dkK-]/g, '')
                // Limitar a 10 caracteres (8 dígitos + guión + dígito verificador)
                validatedValue = validatedValue.slice(0, 10)
                break

            case 'telefono':
                // Este caso ahora se maneja con handleTelefonoChange
                break
        }

        if (name !== 'telefono') {
            setFormData(prev => ({
                ...prev,
                [name]: validatedValue
            }))

            setErrors(prev => ({
                ...prev,
                [name]: error
            }))
        }
    }

    const validateForm = () => {
        const newErrors = {}
        let isValid = true
        const errorFields = []

        // Validación de nombre
        if (!formData.nombre.trim()) {
            newErrors.nombre = 'El nombre es requerido'
            errorFields.push('nombre')
            isValid = false
        }

        // Validación de RUT (formato: 12345678-9)
        if (!formData.rut.trim()) {
            newErrors.rut = 'El RUT es requerido'
            errorFields.push('RUT')
            isValid = false
        } else if (!/^\d{7,8}-[\dkK]$/.test(formData.rut)) {
            newErrors.rut = 'Formato: 12345678-9 (sin puntos)'
            errorFields.push('RUT')
            isValid = false
        }

        // Validación de correo
        if (!formData.correo.trim()) {
            newErrors.correo = 'El correo es requerido'
            errorFields.push('correo')
            isValid = false
        } else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(formData.correo)) {
            newErrors.correo = 'Formato válido: usuario@dominio.extension'
            errorFields.push('correo')
            isValid = false
        }

        // Validación de teléfono
        if (formData.telefono.length < 12) {
            newErrors.telefono = 'Formato completo: +569XXXXXXXX'
            errorFields.push('teléfono')
            isValid = false
        }

        // Validación de fecha de nacimiento
        if (!formData.fechaNacimiento) {
            newErrors.fechaNacimiento = 'La fecha de nacimiento es requerida'
            errorFields.push('fecha de nacimiento')
            isValid = false
        } else {
            const today = new Date()
            const birthDate = new Date(formData.fechaNacimiento)
            const age = today.getFullYear() - birthDate.getFullYear()

            if (age < 8 || age > 120) {
                newErrors.fechaNacimiento = 'Edad debe ser entre 8 y 120 años'
                errorFields.push('fecha de nacimiento')
                isValid = false
            }
        }

        setErrors(newErrors)
        return { isValid, errorFields }
    }

    const showNotification = (message, type) => {
        setNotification({ show: true, message, type })
    }

    const closeNotification = () => {
        setNotification({ ...notification, show: false })
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const { isValid, errorFields } = validateForm();
        if (!isValid) {
            showNotification(`Corrija los errores en: ${errorFields.join(', ')}`, 'warning');
            return;
        }

        setLoading(true);
        try {
            const clienteData = {
                ...formData,
                rut: formData.rut.toUpperCase(),
                // Enviar la fecha exactamente como está en el input (formato YYYY-MM-DD)
                fechaNacimiento: formData.fechaNacimiento
            };

            if (cliente) {
                await updateCliente(cliente.id, clienteData);
                showNotification('Cliente actualizado exitosamente', 'success');
            } else {
                await createCliente(clienteData);
                showNotification('Cliente creado exitosamente', 'success');
            }

            setTimeout(() => {
                onClose();
            }, 2000);
        } catch (error) {
            console.error('Error:', error);
            const errorMessage = error.response?.data?.message ||
                (cliente ? 'No se pudo actualizar el cliente' : 'No se pudo crear el cliente');
            showNotification(errorMessage, 'error');
        } finally {
            setLoading(false);
        }
    };

    const renderInput = (
        name,
        label,
        icon,
        type = 'text',
        defaultValue = '',
        customOnChange = null
    ) => {
        const maxInputLength =
            name === 'telefono' ? 12 : name === 'rut' ? 10 : undefined;

        return (
            <div
                className="form-group"
                style={{ marginBottom: errors[name] ? '2.5rem' : '1.5rem' }}
            >
                <label>
                    {icon && React.cloneElement(icon, { className: 'input-icon' })}
                    {label}
                </label>
                <div className="input-with-error-container">
                    <input
                        type={type}
                        name={name}
                        value={formData[name] || defaultValue}
                        onChange={customOnChange || handleChange}
                        onBlur={handleBlur}
                        className={errors[name] ? 'input-error' : ''}
                        maxLength={maxInputLength}
                    />
                    {errors[name] && (
                        <div className="error-tooltip">
                            <FaExclamationTriangle className="error-icon" />
                            {errors[name]}
                        </div>
                    )}
                </div>
            </div>
        );
    };



    let contenidoBoton;

    if (loading) {
        contenidoBoton = 'Procesando...';
    } else if (cliente) {
        contenidoBoton = (
            <>
                <FaSave className="btn-icon" />
                Actualizar Cliente
            </>
        );
    } else {
        contenidoBoton = (
            <>
                <FaUserPlus className="btn-icon" />
                Crear Cliente
            </>
        );
    }


    return (
        <div className={modoCompacto ? 'clientes-form-compacto' : 'modal-overlay'}>
            {notification.show && (
                <Notification
                    message={notification.message}
                    type={notification.type}
                    onClose={closeNotification}
                />
            )}
            <div className={modoCompacto ? 'clientes-form-contenedor' : 'clientes-form-modal'}>
                <h2 className="titulo-formulario">
                    {cliente ? 'Editar Cliente' : 'Crear Nuevo Cliente'}
                    <button className="close-btn" onClick={onClose}><FaTimes /></button>
                </h2>

                <form onSubmit={handleSubmit}>
                    {modoCompacto ? (
                        <div className="clientes-form-grid">
                            {renderInput('nombre', 'Nombre (*):', <FaIdCard />)}
                            {renderInput('apellido', 'Apellido:', <FaIdCard />)}
                            {renderInput('rut', 'RUT (12345678-9) (*):', <FaIdCard />)}
                            {renderInput('correo', 'Correo electrónico (*):', <FaEnvelope />, 'email')}
                            {renderInput('telefono', 'Teléfono (+569XXXXXXXX) (*):', <FaPhone />, 'tel', '+569', handleTelefonoChange)}
                            {renderInput('fechaNacimiento', 'Fecha de Nacimiento (*):', <FaCalendarAlt />, 'date')}
                        </div>
                    ) : (
                        <>
                            {renderInput('nombre', 'Nombre (*):', <FaIdCard />)}
                            {renderInput('apellido', 'Apellido:', <FaIdCard />)}
                            {renderInput('rut', 'RUT (Formato: 12345678-9) (*):', <FaIdCard />)}
                            {renderInput('correo', 'Correo electrónico (*):', <FaEnvelope />, 'email')}
                            {renderInput('telefono', 'Teléfono (Formato: +569XXXXXXXX) (*):', <FaPhone />, 'tel', '+569', handleTelefonoChange)}
                            {renderInput('fechaNacimiento', 'Fecha de Nacimiento (*):', <FaCalendarAlt />, 'date')}
                        </>
                    )}

                    <div className="form-actions">
                        <button disabled={loading} className="submit-btn">
                            {contenidoBoton}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

ClientesForm.propTypes = {
    cliente: PropTypes.shape({
        id: PropTypes.number,
        nombre: PropTypes.string,
        apellido: PropTypes.string,
        rut: PropTypes.string,
        correo: PropTypes.string,
        telefono: PropTypes.string,
        activo: PropTypes.bool,
        fechaNacimiento: PropTypes.string
    }),
    onClose: PropTypes.func.isRequired,
    modoCompacto: PropTypes.bool
};

ClientesForm.defaultProps = {
    modoCompacto: false
};

export default ClientesForm;