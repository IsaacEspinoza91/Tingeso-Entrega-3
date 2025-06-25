import React, { useState, useEffect } from 'react';
import { FaTimes, FaUser, FaIdCard, FaEnvelope, FaPhone, FaCalendarAlt } from 'react-icons/fa';

export default function ClienteForm({ cliente, onClose, showNotification }) {
    const [formData, setFormData] = useState({
        rut: '',
        nombre: '',
        apellido: '',
        correo: '',
        telefono: '',
        fechaNacimiento: '',
        activo: true
    });
    const [errors, setErrors] = useState({});
    const [touched, setTouched] = useState({});
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (cliente) {
            setFormData({
                rut: cliente.rut,
                nombre: cliente.nombre,
                apellido: cliente.apellido,
                correo: cliente.correo,
                telefono: cliente.telefono,
                fechaNacimiento: cliente.fechaNacimiento.split('T')[0],
                activo: cliente.activo
            });
        }
    }, [cliente]);

    const validateField = (field, value) => {
        let error = '';

        if (!value) {
            error = 'Este campo es requerido';
        } else if (field === 'correo' && !/^\S+@\S+\.\S+$/.test(value)) {
            error = 'Correo no válido';
        } else if (field === 'rut' && !/^(\d{1,3}(?:\.?\d{3}){2}-[\dkK])$/.test(value)) {
            error = 'RUT no válido';
        }

        setErrors({ ...errors, [field]: error });
        return !error;
    };

    const handleBlur = (field) => {
        setTouched({ ...touched, [field]: true });
        validateField(field, formData[field]);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        if (touched[name]) {
            validateField(name, value);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Marcar todos los campos como tocados
        const allTouched = Object.keys(formData).reduce((acc, field) => {
            acc[field] = true;
            return acc;
        }, {});
        setTouched(allTouched);

        // Validar todos los campos
        const isValid = Object.keys(formData).every(field =>
            validateField(field, formData[field])
        );

        if (!isValid) {
            showNotification('Complete todos los campos correctamente', 'warning');
            return;
        }

        setLoading(true);
        try {
            // Aquí iría la llamada a tu servicio para crear/actualizar
            // await (cliente ? updateCliente : createCliente)(formData);

            showNotification(
                cliente ? 'Cliente actualizado exitosamente' : 'Cliente creado exitosamente',
                'success'
            );

            setTimeout(() => {
                onClose();
            }, 1500);
        } catch (error) {
            console.error('Error:', error);
            showNotification(
                cliente ? 'Error al actualizar cliente' : 'Error al crear cliente',
                'error'
            );
        } finally {
            setLoading(false);
        }
    };

    const renderInput = (name, label, icon, type = 'text') => (
        <div className="form-group">
            <label>
                {icon}
                {label}
            </label>
            <div className="input-container">
                <input
                    type={type}
                    name={name}
                    value={formData[name]}
                    onChange={handleChange}
                    onBlur={() => handleBlur(name)}
                    className={errors[name] && touched[name] ? 'input-error' : ''}
                />
                {errors[name] && touched[name] && (
                    <div className="error-tooltip">
                        <FaExclamationTriangle className="error-icon" />
                        {errors[name]}
                    </div>
                )}
            </div>
        </div>
    );

    return (
        <div className="modal-overlay">
            <div className="cliente-form-modal">
                <h2>{cliente ? 'Editar Cliente' : 'Crear Nuevo Cliente'}</h2>
                <button className="close-btn" onClick={onClose}>×</button>

                <form onSubmit={handleSubmit}>
                    {renderInput('rut', 'RUT:', <FaIdCard className="input-icon" />)}
                    {renderInput('nombre', 'Nombre:', <FaUser className="input-icon" />)}
                    {renderInput('apellido', 'Apellido:', <FaUser className="input-icon" />)}
                    {renderInput('correo', 'Correo:', <FaEnvelope className="input-icon" />, 'email')}
                    {renderInput('telefono', 'Teléfono:', <FaPhone className="input-icon" />, 'tel')}
                    {renderInput('fechaNacimiento', 'Fecha Nacimiento:', <FaCalendarAlt className="input-icon" />, 'date')}

                    <div className="form-actions">
                        <button type="button" onClick={onClose} className="cancel-btn">
                            <FaTimes className="btn-icon" />
                            Cancelar
                        </button>
                        <button type="submit" disabled={loading} className="submit-btn">
                            {loading ? 'Procesando...' : (cliente ? 'Actualizar' : 'Crear')}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}