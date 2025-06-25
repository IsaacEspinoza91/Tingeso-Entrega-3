import React, { useState, useEffect } from 'react'
import { createPlan, updatePlan } from '../../services/planService'
import { FaSave, FaPlus, FaClock, FaMoneyBillWave, FaTimes, FaListAlt, FaExclamationTriangle } from 'react-icons/fa'
import Notification from '../Notification'

export default function PlanForm({ plan, onClose }) {
    const [formData, setFormData] = useState({
        descripcion: '',
        duracionTotal: '',
        precioRegular: '',
        precioFinSemana: '',
        precioFeriado: ''
    });
    const [errors, setErrors] = useState({});
    const [touched, setTouched] = useState({});
    const [loading, setLoading] = useState(false);
    const [notification, setNotification] = useState({ show: false, message: '', type: '' });

    useEffect(() => {
        if (plan) {
            setFormData({
                descripcion: plan.descripcion,
                duracionTotal: plan.duracionTotal.toString(),
                precioRegular: plan.precioRegular.toString(),
                precioFinSemana: plan.precioFinSemana.toString(),
                precioFeriado: plan.precioFeriado.toString()
            });
        }
    }, [plan]);

    // Manejar el evento blur (cuando el input pierde el foco)
    const handleBlur = (field) => {
        setTouched({ ...touched, [field]: true });
        validateField(field, formData[field]);
    };

    const validateField = (field, value) => {
        let error = '';

        if (field === 'descripcion') {
            if (!value.trim()) error = 'La descripción es requerida';
        } else {
            if (!value && value !== 0) {
                error = 'Este campo es requerido';
            } else if (!/^\d*\.?\d+$/.test(value)) {
                error = 'Solo se permiten números positivos';
            } else if (parseFloat(value) < 0) {
                error = 'El valor no puede ser negativo';
            } else if (field === 'duracionTotal' && parseFloat(value) <= 0) {
                error = 'La duración debe ser mayor a 0';
            }
        }

        setErrors({ ...errors, [field]: error });
        return !error;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;

        // Para campos numéricos, permitir solo números enteros
        if (name !== 'descripcion') {
            // Solo permitir dígitos (0-9)
            if (!/^\d*$/.test(value)) {
                setErrors({ ...errors, [name]: 'Solo se permiten números enteros positivos' });
                return;
            }
        }

        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        // Limpiar error si se corrige la entrada
        if (errors[name] && /^\d+$/.test(value)) {
            setErrors({ ...errors, [name]: '' });
        }

        // Validación en tiempo real si el campo ya fue tocado
        if (touched[name]) {
            validateField(name, value);
        }
    };

    const validateForm = () => {
        let valid = true;
        const newErrors = {};

        Object.keys(formData).forEach(field => {
            if (!validateField(field, formData[field])) {
                valid = false;
            }
        });

        return valid;
    };

    const showNotification = (message, type) => {
        setNotification({ show: true, message, type });
    };

    const closeNotification = () => {
        setNotification({ ...notification, show: false });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Marcar todos los campos como tocados
        const allTouched = Object.keys(formData).reduce((acc, field) => {
            acc[field] = true;
            return acc;
        }, {});
        setTouched(allTouched);

        // Validar campos vacíos primero
        const emptyFields = Object.entries(formData)
            .filter(([field, value]) => !value && field !== 'descripcion')
            .map(([field]) => field);

        if (emptyFields.length > 0) {
            const fieldNames = {
                duracionTotal: 'Duración',
                precioRegular: 'Precio Regular',
                precioFinSemana: 'Precio Fin de Semana',
                precioFeriado: 'Precio Feriado'
            };

            const missingFields = emptyFields.map(field => fieldNames[field]).join(', ');
            showNotification(`Complete los campos requeridos: ${missingFields}`, 'warning');
            return;
        }

        if (!validateForm()) {
            showNotification('Corrija los errores en el formulario antes de enviar', 'warning');
            return;
        }

        setLoading(true);
        try {
            const numericData = {
                ...formData,
                duracionTotal: parseFloat(formData.duracionTotal),
                precioRegular: parseFloat(formData.precioRegular),
                precioFinSemana: parseFloat(formData.precioFinSemana),
                precioFeriado: parseFloat(formData.precioFeriado)
            };

            if (plan) {
                await updatePlan(plan.id, numericData);
                showNotification('Plan actualizado exitosamente', 'success');
            } else {
                await createPlan(numericData);
                showNotification('Plan creado exitosamente', 'success');
            }

            // Cerrar el modal después de 2 segundos
            setTimeout(() => {
                onClose();
            }, 2000);
        } catch (error) {
            console.error('Error:', error);
            showNotification(
                plan ? 'No se pudo actualizar el plan' : 'No se pudo crear el plan',
                'error'
            );
        } finally {
            setLoading(false);
        }
    };

    const renderInput = (name, label, icon, type = 'text', min = undefined) => (
        <div className="form-group">
            <label>
                {icon && React.cloneElement(icon, { className: 'input-icon' })}
                {label}
            </label>
            <div className="input-container">
                <input
                    type={type}
                    name={name}
                    value={formData[name]}
                    onChange={handleChange}
                    onBlur={() => handleBlur(name)}
                    min={min}
                    step={type === 'number' ? '1' : undefined}
                    className={errors[name] ? 'input-error' : ''}
                    onKeyDown={(e) => {
                        // Permitir solo teclas numéricas y comandos
                        if (name !== 'descripcion' && !/[0-9]|Backspace|Delete|ArrowLeft|ArrowRight|Tab/.test(e.key)) {
                            e.preventDefault();
                            setErrors({ ...errors, [name]: 'Solo se permiten números positivos' });
                        }
                    }}
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

    return (
        <div className="modal-overlay">
            {notification.show && (
                <Notification
                    message={notification.message}
                    type={notification.type}
                    onClose={closeNotification}
                />
            )}
            <div className="plan-form-modal">
                <h2>{plan ? 'Editar Plan' : 'Crear Nuevo Plan'}</h2>
                <button className="close-btn" onClick={onClose}>×</button>

                <form onSubmit={handleSubmit} noValidate>
                    {renderInput('descripcion', 'Descripción:', <FaListAlt />)}

                    {renderInput('duracionTotal', 'Duración (minutos):', <FaClock />, 'number', '1')}

                    {renderInput('precioRegular', 'Precio Regular:', <FaMoneyBillWave />, 'number', '0')}

                    {renderInput('precioFinSemana', 'Precio Fin de Semana:', <FaMoneyBillWave />, 'number', '0')}

                    {renderInput('precioFeriado', 'Precio Feriado:', <FaMoneyBillWave />, 'number', '0')}
                    <div className="form-actions">
                        <button onClick={onClose} className="cancel-btn">
                            <FaTimes className="btn-icon" />
                            Cancelar
                        </button>
                        <button disabled={loading} className="submit-btn">
                            {loading ? (
                                <span>Procesando...</span>
                            ) : plan ? (
                                <>
                                    <FaSave className="btn-icon" />
                                    <span>Actualizar Plan</span>
                                </>
                            ) : (
                                <>
                                    <FaPlus className="btn-icon" />
                                    <span>Crear Plan</span>
                                </>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}