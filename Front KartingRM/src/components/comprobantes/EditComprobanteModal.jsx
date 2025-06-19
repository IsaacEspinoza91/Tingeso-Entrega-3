import React, { useState, useEffect } from 'react';
import { updateComprobante } from '../../services/comprobanteService';
import './EditComprobanteModal.css';

const EditComprobanteModal = ({ comprobante, onClose, onUpdate }) => {
    const [pagado, setPagado] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (comprobante) {
            setPagado(comprobante.pagado || false);
        }
    }, [comprobante]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        setIsSubmitting(true);
        setError('');

        try {
            // El servicio ahora solo actualiza el estado de pago
            const updatedComprobante = await updateComprobante(comprobante.id, pagado);

            // Construir objeto actualizado con todos los datos necesarios
            const fullUpdatedComprobante = {
                ...comprobante,
                pagado: updatedComprobante.pagado
            };

            onUpdate(fullUpdatedComprobante);
            onClose();
        } catch (err) {
            console.error('Error al actualizar comprobante:', err);
            setError(err.response.data);
        } finally {
            setIsSubmitting(false);
        }
    };

    if (!comprobante) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <h3>Editar Estado de Pago - Comprobante #{comprobante.id}</h3>
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
                        <label className="checkbox-label">
                            <input
                                type="checkbox"
                                checked={pagado}
                                onChange={(e) => setPagado(e.target.checked)}
                                disabled={isSubmitting}
                            />
                            <span className="checkbox-custom"></span>
                            <span className="checkbox-text">Marcar como pagado</span>
                        </label>
                    </div>

                    {error && <div className="error-message">{error}</div>}

                    <div className="comprobante-summary">
                        <div className="summary-item">
                            <span className="summary-label">Total:</span>
                            <span>${comprobante.total.toLocaleString()}</span>
                        </div>
                        <div className="summary-item">
                            <span className="summary-label">Reserva ID:</span>
                            <span>{comprobante.reserva.id}</span>
                        </div>
                        <div className="summary-item">
                            <span className="summary-label">Fecha:</span>
                            <span>{new Date(comprobante.reserva.fecha + "T00:00:00").toLocaleDateString()}</span>
                        </div>
                    </div>

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
                            className="save-button"
                        >
                            {isSubmitting ? 'Guardando...' : 'Guardar Cambios'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default EditComprobanteModal;