import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { FaFileAlt, FaCalendarAlt, FaFileContract } from 'react-icons/fa';

const meses = [
  { value: 1, label: 'Enero' },
  { value: 2, label: 'Febrero' },
  { value: 3, label: 'Marzo' },
  { value: 4, label: 'Abril' },
  { value: 5, label: 'Mayo' },
  { value: 6, label: 'Junio' },
  { value: 7, label: 'Julio' },
  { value: 8, label: 'Agosto' },
  { value: 9, label: 'Septiembre' },
  { value: 10, label: 'Octubre' },
  { value: 11, label: 'Noviembre' },
  { value: 12, label: 'Diciembre' }
];

const ReportesForm = ({ onGenerarReporte, loading }) => {
  const currentYear = new Date().getFullYear();
  const [formData, setFormData] = useState({
    tipoReporte: 'vueltas',
    mesInicio: 1,
    anioInicio: currentYear,
    mesFin: 12,
    anioFin: currentYear
  });
  const [errorFecha, setErrorFecha] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    const newFormData = {
      ...formData,
      [name]: name.includes('anio') ? parseInt(value) : value
    };

    setFormData(newFormData);

    // Validar fechas cada vez que cambian
    const fechaInicio = new Date(newFormData.anioInicio, newFormData.mesInicio - 1);
    const fechaFin = new Date(newFormData.anioFin, newFormData.mesFin - 1);

    if (fechaInicio > fechaFin) {
      setErrorFecha('La fecha de inicio no puede ser posterior a la fecha de fin');
    } else {
      setErrorFecha('');
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Validar fechas antes de enviar
    const fechaInicio = new Date(formData.anioInicio, formData.mesInicio - 1);
    const fechaFin = new Date(formData.anioFin, formData.mesFin - 1);

    if (fechaInicio > fechaFin) {
      setErrorFecha('La fecha de inicio no puede ser posterior a la fecha de fin');
      return;
    }

    onGenerarReporte(formData);
  };

  return (
    <div className="reportes-form">
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <div className="reportes-icon-header">
            <FaFileAlt className="reportes-icon" />
            <span>Tipo de Reporte:</span>
          </div>
          <select
            name="tipoReporte"
            value={formData.tipoReporte}
            onChange={handleChange}
            className="form-control"
          >
            <option value="vueltas">Ingresos por número de vueltas o tiempo máximo</option>
            <option value="personas">Ingresos por número de personas</option>
          </select>
        </div>

        <div className="date-range-container">
          <div className="date-range-header">
            <FaCalendarAlt className="date-icon" />
            <span>Rango de Fechas</span>
          </div>

          <div className="date-range">
            <div className="form-group">
              <label htmlFor="mesInicio">Fecha Inicio:</label>
              <div className="date-inputs">
                <select
                  name="mesInicio"
                  value={formData.mesInicio}
                  onChange={handleChange}
                  className="form-control"
                >
                  {meses.map(mes => (
                    <option key={mes.value} value={mes.value}>{mes.label}</option>
                  ))}
                </select>
                <input
                  type="number"
                  name="anioInicio"
                  value={formData.anioInicio}
                  onChange={handleChange}
                  min="2020"
                  max="2050"
                  className="form-control"
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="mesFin">Fecha Fin:</label>
              <div className="date-inputs">
                <select
                  name="mesFin"
                  value={formData.mesFin}
                  onChange={handleChange}
                  className="form-control"
                >
                  {meses.map(mes => (
                    <option key={mes.value} value={mes.value}>{mes.label}</option>
                  ))}
                </select>
                <input
                  type="number"
                  name="anioFin"
                  value={formData.anioFin}
                  onChange={handleChange}
                  min="2000"
                  max="2050"
                  className="form-control"
                />
              </div>
            </div>
          </div>

          {errorFecha && (
            <div className="error-message" style={{ color: 'red', marginTop: '10px' }}>
              {errorFecha}
            </div>
          )}
        </div>

        <div className="form-actions">
          <button
            type="submit"
            className="submit-btn"
            disabled={loading || errorFecha}
          >
            <FaFileContract /> {loading ? 'Generando...' : 'Generar Reporte'}
          </button>
        </div>
      </form>
    </div>
  );
};

ReportesForm.propTypes = {
  onGenerarReporte: PropTypes.func.isRequired,
  loading: PropTypes.bool.isRequired
};

export default ReportesForm;