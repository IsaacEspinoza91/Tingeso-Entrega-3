import React, { useState, useEffect } from 'react';
import { updateReserva } from '../../services/reservaService';
import './EditReservaModal.css';

const estadosReserva = [
  'confirmada',
  'cancelada',
  'completada'
];

const EditReservaModal = ({ reserva, onClose, onUpdate }) => {
  const [formData, setFormData] = useState({
    estado: 'confirmada',
    totalPersonas: 1,
    fecha: '',
    horaInicio: '',
    idReservante: '',
    idPlan: ''
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (reserva) {
      setFormData({
        estado: reserva.estado || 'confirmada',
        totalPersonas: reserva.totalPersonas || 1,
        fecha: reserva.fecha || '',
        horaInicio: reserva.horaInicio || '',
        idReservante: reserva.reservante?.id || '',
        idPlan: reserva.plan?.id || ''
      });
    }
  }, [reserva]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === 'totalPersonas' || name === 'idReservante' || name === 'idPlan'
        ? Number(value)
        : value
    });
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.estado) newErrors.estado = 'Estado es requerido';
    if (!formData.totalPersonas || formData.totalPersonas < 1) newErrors.totalPersonas = 'Número de personas inválido';
    if (!formData.fecha) newErrors.fecha = 'Fecha es requerida';
    if (!formData.horaInicio) newErrors.horaInicio = 'Hora de inicio es requerida';
    if (!formData.idReservante) newErrors.idReservante = 'ID Reservante es requerido';
    if (!formData.idPlan) newErrors.idPlan = 'ID Plan es requerido';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    setIsSubmitting(true);
    try {
      const reservaData = {
        fecha: formData.fecha,
        horaInicio: formData.horaInicio,
        estado: formData.estado,
        totalPersonas: formData.totalPersonas,
        idPlan: formData.idPlan,
        idReservante: formData.idReservante
      };

      await updateReserva(reserva.id, reservaData);

      // Crear objeto actualizado manualmente si el backend no devuelve todos los datos
      const updatedReserva = {
        ...reserva,
        ...reservaData,
        plan: { id: formData.idPlan, ...reserva.plan },
        reservante: { id: formData.idReservante, ...reserva.reservante }
      };

      onUpdate(updatedReserva);
      onClose();
    } catch (error) {
      console.error('Error al actualizar reserva:', error);
      setErrors({ submit: 'Error al actualizar reserva. Por favor intente nuevamente.' });
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!reserva) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h3>Editar Reserva #{reserva.id}</h3>
          <button onClick={onClose} className="close-button">&times;</button>
        </div>

        <form onSubmit={handleSubmit}></form>

        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>ID Cliente Reservante:</label>
              <input
                type="number"
                name="idReservante"
                value={formData.idReservante || ''}
                onChange={handleChange}
                min="1"
              />
              {errors.idReservante && <span className="error">{errors.idReservante}</span>}
            </div>

            <div className="form-group">
              <label>ID Plan:</label>
              <input
                type="number"
                name="idPlan"
                value={formData.idPlan || ''}
                onChange={handleChange}
                min="1"
              />
              {errors.idPlan && <span className="error">{errors.idPlan}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Fecha:</label>
              <input
                type="date"
                name="fecha"
                value={formData.fecha}
                onChange={handleChange}
              />
              {errors.fecha && <span className="error">{errors.fecha}</span>}
            </div>

            <div className="form-group">
              <label>Hora Inicio:</label>
              <input
                type="time"
                name="horaInicio"
                value={formData.horaInicio}
                onChange={handleChange}
              />
              {errors.horaInicio && <span className="error">{errors.horaInicio}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Estado:</label>
              <select
                name="estado"
                value={formData.estado}
                onChange={handleChange}
              >
                {estadosReserva.map(estado => (
                  <option key={estado} value={estado}>
                    {estado.charAt(0).toUpperCase() + estado.slice(1)}
                  </option>
                ))}
              </select>
              {errors.estado && <span className="error">{errors.estado}</span>}
            </div>

            <div className="form-group">
              <label>Total Personas:</label>
              <input
                type="number"
                name="totalPersonas"
                value={formData.totalPersonas}
                onChange={handleChange}
                min="1"
              />
              {errors.totalPersonas && <span className="error">{errors.totalPersonas}</span>}
            </div>
          </div>

          {errors.submit && <div className="error-message">{errors.submit}</div>}

          <div className="modal-actions">
            <button type="button" onClick={onClose} className="cancel-button">
              Cancelar
            </button>
            <button type="submit" disabled={isSubmitting} className="save-button">
              {isSubmitting ? 'Guardando...' : 'Guardar Cambios'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditReservaModal;