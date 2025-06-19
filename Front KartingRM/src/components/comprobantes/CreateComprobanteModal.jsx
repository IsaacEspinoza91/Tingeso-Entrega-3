import React, { useState } from 'react';
import { createComprobante } from '../../services/comprobanteService';
import './CreateComprobanteModal.css';

const CreateComprobanteModal = ({ onClose, onComprobanteCreated }) => {
    const [formData, setFormData] = useState({
        idReserva: '',
        descuentoExtra: 0
    });

    const [errors, setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: name === 'descuentoExtra' || name === 'idReserva'
                ? Number(value)
                : value
        });
    };

    const validateForm = () => {
        const newErrors = {};

        if (!formData.idReserva || formData.idReserva <= 0) {
            newErrors.idReserva = 'ID de Reserva es requerido y debe ser mayor a 0';
        }
        if (formData.descuentoExtra < 0) {
            newErrors.descuentoExtra = 'Descuento no puede ser negativo';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) return;

        setIsSubmitting(true);
        setErrorMessage('');

        try {
            const response = await createComprobante(
                formData.idReserva,
                formData.descuentoExtra
            );

            onComprobanteCreated(response);
            setErrorMessage('');
            onClose();
        } catch (err) {
            setErrorMessage(err.response.data);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <h3>Crear Nuevo Comprobante</h3>
                    <button
                        onClick={onClose}
                        className="close-button"
                        disabled={isSubmitting}
                    >
                        &times;
                    </button>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>ID de Reserva:</label>
                        <input
                            type="number"
                            name="idReserva"
                            value={formData.idReserva || ''}
                            onChange={handleChange}
                            min="1"
                            placeholder="Ingrese ID de reserva"
                            disabled={isSubmitting}
                        />
                        {errors.idReserva && <span className="error">{errors.idReserva}</span>}
                    </div>

                    <div className="form-group">
                        <label>Descuento Extra ($):</label>
                        <input
                            type="number"
                            name="descuentoExtra"
                            value={formData.descuentoExtra}
                            onChange={handleChange}
                            min="0"
                            step="0.01"
                            disabled={isSubmitting}
                        />
                        {errors.descuentoExtra && <span className="error">{errors.descuentoExtra}</span>}
                    </div>

                    {errorMessage && <div className="error-message">{errorMessage}</div>}

                    <div className="modal-actions">
                        <button
                            type="button"
                            onClick={onClose}
                            className="cancel-button"
                            disabled={isSubmitting}
                        >
                            Cancelar
                        </button>
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="submit-button"
                        >
                            {isSubmitting ? 'Creando...' : 'Crear Comprobante'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateComprobanteModal;