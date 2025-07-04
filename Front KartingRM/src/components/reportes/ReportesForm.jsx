import React, { useState } from 'react';
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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name.includes('anio') ? parseInt(value) : value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
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
            <option value="vueltas">Ingresos por vueltas</option>
            <option value="personas">Ingresos por personas</option>
          </select>
        </div>

        <div className="date-range-container">
          <div className="date-range-header">
            <FaCalendarAlt className="date-icon" />
            <span>Rango de Fechas</span>
          </div>

          <div className="date-range">
            <div className="form-group">
              <label>Fecha Inicio:</label>
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
                  max="2100"
                  className="form-control"
                />
              </div>
            </div>

            <div className="form-group">
              <label>Fecha Fin:</label>
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
                  max="2100"
                  className="form-control"
                />
              </div>
            </div>
          </div>
        </div>

        <div className="form-actions">
          <button
            type="submit"
            className="submit-btn"
            disabled={loading}
          >
            <FaFileContract /> {loading ? 'Generando...' : 'Generar Reporte'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default ReportesForm;