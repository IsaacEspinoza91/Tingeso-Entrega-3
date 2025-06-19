import React, { useState } from 'react';
import { addIntegrante } from '../../services/reservaService';

const AddIntegranteModal = ({ reserva, onClose, onIntegranteAdded }) => {
  const [idCliente, setIdCliente] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!idCliente || isNaN(idCliente) || idCliente <= 0) {
      setError('Por favor ingrese un ID de cliente válido');
      return;
    }

    setIsSubmitting(true);
    setError(null);
    setSuccessMessage('');

    try {
      await addIntegrante(reserva.id, idCliente);
      setSuccessMessage('Integrante agregado exitosamente!');
      setIdCliente('');

      // Notificar al componente padre que se agregó un integrante
      if (onIntegranteAdded) {
        onIntegranteAdded();
      }

      // Ocultar el mensaje de éxito después de 3 segundos
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (err) {
      console.error('Error al agregar integrante:', err);
      setError(err.response?.data?.message || 'Error al agregar integrante. Verifique el ID del cliente.');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!reserva) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content add-integrante-modal">
        <div className="modal-header">
          <h3>Agregar Integrante a Reserva #{reserva.id}</h3>
          <button onClick={onClose} className="close-button">&times;</button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>ID del Cliente a agregar:</label>
            <input
              type="number"
              value={idCliente}
              onChange={(e) => {
                const value = e.target.value;
                if (value === '' || /^[0-9]*$/.test(value)) {
                  setIdCliente(value);
                }
              }}
              placeholder="Ingrese ID numérico del cliente"
              min="1"
            />
          </div>

          {error && <div className="error-message">{error}</div>}
          {successMessage && <div className="success-message">{successMessage}</div>}

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
              disabled={isSubmitting || !idCliente}
              className="save-button"
            >
              {isSubmitting ? 'Agregando...' : 'Agregar Integrante'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddIntegranteModal;