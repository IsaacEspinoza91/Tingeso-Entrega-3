import React, { useState } from 'react';
import {
  getReporteIngresosPorVueltas,
  getReporteIngresosPorPersonas
} from '../../services/reporteService';
import ReportesForm from '../../components/reportes/ReportesForm';
import ReportesTable from '../../components/reportes/ReportesTable';
import { FaHome, FaExclamationTriangle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import './Reportes.css';

const Reportes = () => {
  const [reporteData, setReporteData] = useState([]);
  const [tipoReporte, setTipoReporte] = useState('vueltas');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const generarReporte = async (formData) => {
    setLoading(true);
    setError(null);
    try {
      let data;
      const params = {
        mes_inicio: formData.mesInicio,
        anio_inicio: formData.anioInicio,
        mes_fin: formData.mesFin,
        anio_fin: formData.anioFin
      };

      if (formData.tipoReporte === 'vueltas') {
        data = await getReporteIngresosPorVueltas(params);
        setTipoReporte('vueltas');
      } else {
        data = await getReporteIngresosPorPersonas(params);
        setTipoReporte('personas');
      }

      setReporteData(data);
    } catch (err) {
      setError('Error al generar el reporte. Por favor intente nuevamente.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="reportes-page">
      <div className="reportes-container">
        <div className="reportes-header">
          <div className="content-header">
            <h1>Reportes de Ingresos</h1>
            <button
              onClick={() => navigate('/')}
              className="floating-home-btn"
              aria-label="Volver al inicio"
            >
              <FaHome />
              <span>Inicio</span>
            </button>
          </div>
        </div>

        <div className="reportes-content">
          <ReportesForm
            onGenerarReporte={generarReporte}
            loading={loading}
          />

          {error && (
            <div className="error-feedback">
              <FaExclamationTriangle className="error-icon" />
              {error}
            </div>
          )}

          {loading ? (
            <div className="loading-message">Generando reporte...</div>
          ) : reporteData.length > 0 ? (
            <div className="reportes-list-container">
              <ReportesTable
                data={reporteData}
                tipoReporte={tipoReporte}
              />
            </div>
          ) : (
            <div className="no-results">
              No hay datos para mostrar. Genere un reporte para ver los resultados.
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Reportes;