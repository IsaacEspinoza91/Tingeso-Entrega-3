import React, { useState } from 'react';
import { createReserva } from '../../services/reservaService';
import './CreateReservaForm.css';

const CreateReservaForm = ({ onReservaCreated }) => {
  const [formData, setFormData] = useState({
    fecha: '',
    horaInicio: '',
    estado: 'confirmada',
    totalPersonas: 1,
    idPlan: '',
    idReservante: ''
  });
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === 'totalPersonas' || name === 'idPlan' || name === 'idReservante'
        ? Number(value)
        : value
    });
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.idReservante || formData.idReservante <= 0) newErrors.idReservante = 'ID Cliente es requerido';
    if (!formData.idPlan || formData.idPlan <= 0) newErrors.idPlan = 'ID Plan es requerido';
    if (!formData.estado) newErrors.estado = 'Estado es requerido';
    if (formData.totalPersonas <= 0) newErrors.totalPersonas = 'Debe haber al menos 1 persona';
    if (!formData.fecha) newErrors.fecha = 'Fecha es requerida';
    if (!formData.horaInicio) newErrors.horaInicio = 'Hora de inicio es requerida';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    setIsSubmitting(true);
    try {
      // Preparamos el objeto según lo que espera el backend
      const reservaData = {
        fecha: formData.fecha,
        horaInicio: formData.horaInicio,
        estado: formData.estado,
        totalPersonas: formData.totalPersonas,
        idPlan: formData.idPlan,
        idReservante: formData.idReservante
      };

      await createReserva(reservaData);
      setSuccessMessage('Reserva creada exitosamente!');

      // Resetear el formulario
      setFormData({
        fecha: '',
        horaInicio: '',
        estado: 'confirmada',
        totalPersonas: 1,
        idPlan: '',
        idReservante: ''
      });

      if (onReservaCreated) {
        onReservaCreated();
      }

      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (error) {
      console.error('Error al crear reserva:', error);
      setErrors({ submit: 'Error al crear reserva. Verifique los datos e intente nuevamente.' });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="create-reserva-form">
      <h3>Crear Nueva Reserva</h3>
      {successMessage && <div className="success-message">{successMessage}</div>}
      {errors.submit && <div className="error-message">{errors.submit}</div>}

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
            <label>Estado:</label>
            <select
              name="estado"
              value={formData.estado}
              onChange={handleChange}
            >
              <option value="confirmada">Confirmada</option>
              <option value="cancelada">Cancelada</option>
              <option value="completada">Completada</option>
            </select>
            {errors.estado && <span className="error">{errors.estado}</span>}
          </div>

          <div className="form-group">
            <label>Total de Personas:</label>
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

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Creando...' : 'Crear Reserva'}
        </button>
      </form>
    </div>
  );
};

export default CreateReservaForm;