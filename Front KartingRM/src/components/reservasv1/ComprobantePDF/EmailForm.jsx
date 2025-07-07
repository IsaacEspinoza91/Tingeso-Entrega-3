import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { pdf } from '@react-pdf/renderer';
import ComprobantePDF from './ComprobantePDF';
import httpClient from '../../../http-common';
import { FaEnvelope, FaPaperPlane, FaSpinner } from 'react-icons/fa';
import './EmailForm.css';

const URL = '/api/reservas-comprobantes-service/email/send-pdf';

const EmailForm = ({ comprobante, onClose }) => {
  const [email, setEmail] = useState(comprobante.reserva.reservante.correo || '');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [subject, setSubject] = useState(
    `Comprobante de Pago | Reserva: ${comprobante.reserva.fecha} - ${comprobante.reserva.horaInicio}`
  );

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const pdfBlob = await pdf(<ComprobantePDF comprobante={comprobante} />).toBlob();

      const formData = new FormData();
      formData.append('pdf', pdfBlob, `comprobante-${comprobante.idComprobante}.pdf`);
      formData.append('email', email);
      formData.append('subject', subject);

      const config = {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      };

      await httpClient.post(URL, formData, config);

      setSuccess('El comprobante ha sido enviado exitosamente!');
      setTimeout(onClose, 2000);
    } catch (err) {
      console.error('Error al enviar el correo:', err);
      setError(
        err.response?.data?.message ||
        err.message ||
        'Ocurrió un error al enviar el correo. Por favor intente nuevamente.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="email-form-panel">
      <button className="close-btn" onClick={onClose} disabled={loading}>×</button>

      <h3><FaEnvelope /> Enviar Comprobante por Correo</h3>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="email">Correo electrónico:</label>
          <input
            id="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            disabled={loading}
          />
        </div>

        <div className="form-group">
          <label htmlFor="subject">Asunto:</label>
          <input
            id="subject"
            type="text"
            value={subject}
            onChange={(e) => setSubject(e.target.value)}
            disabled={loading}
          />
        </div>

        {error && (
          <div className="alert alert-error">
            <strong>Error:</strong> {error}
          </div>
        )}

        {success && (
          <div className="alert alert-success">
            <strong>Éxito:</strong> {success}
          </div>
        )}

        <div className="modal-actions">
          <button className="btn-submit" disabled={loading}>
            {loading ? (
              <>
                <FaSpinner className="spinner" /> Enviando...
              </>
            ) : (
              <>
                <FaPaperPlane /> Enviar
              </>
            )}
          </button>
        </div>
      </form>
    </div>
  );
};



EmailForm.propTypes = {
  comprobante: PropTypes.shape({
    idComprobante: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    reserva: PropTypes.shape({
      fecha: PropTypes.string.isRequired,
      horaInicio: PropTypes.string.isRequired,
      reservante: PropTypes.shape({
        correo: PropTypes.string
      }).isRequired
    }).isRequired
  }).isRequired,
  onClose: PropTypes.func.isRequired
};


export default EmailForm;
