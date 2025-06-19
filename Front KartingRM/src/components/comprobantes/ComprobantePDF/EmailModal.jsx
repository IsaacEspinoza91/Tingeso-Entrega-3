import React, { useState } from 'react';
import { pdf } from '@react-pdf/renderer';
import ComprobantePDF from './ComprobantePDF';
import httpClient from '../../../http-common';

const URL = '/api/reservas-comprobantes-service/email/send-pdf';

const EmailModal = ({ comprobante, onClose }) => {
    const [email, setEmail] = useState(comprobante.reserva.reservante.correo || '');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [subject, setSubject] = useState(`Comprobante de Pago | Reserva: ${comprobante.reserva.fecha} - ${comprobante.reserva.horaInicio}`);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

        try {
            // Generar el PDF como Blob
            const pdfBlob = await pdf(<ComprobantePDF comprobante={comprobante} />).toBlob();

            // Crear FormData para el envío
            const formData = new FormData();
            formData.append('pdf', pdfBlob, `comprobante-${comprobante.idComprobante}.pdf`);
            formData.append('email', email);
            formData.append('subject', subject);

            // Configurar headers para FormData
            const config = {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            };

            // Enviar al endpoint del backend
            const response = await httpClient.post(URL, formData, config);

            setSuccess('El comprobante ha sido enviado exitosamente!');
            setTimeout(onClose, 2000);
        } catch (err) {
            console.error('Error al enviar el correo:', err);
            setError(err.response?.data?.message ||
                err.message ||
                'Ocurrió un error al enviar el correo. Por favor intente nuevamente.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="modal-overlay">
            <div className="email-modal">
                <button className="close-button" onClick={onClose} disabled={loading}>
                    &times;
                </button>

                <h3>Enviar Comprobante por Correo</h3>

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
                        <button
                            type="button"
                            className="btn-cancel"
                            onClick={onClose}
                            disabled={loading}
                        >
                            Cancelar
                        </button>

                        <button
                            type="submit"
                            className="btn-submit"
                            disabled={loading}
                        >
                            {loading ? (
                                <>
                                    <span className="spinner"></span> Enviando...
                                </>
                            ) : (
                                'Enviar Comprobante'
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default EmailModal;