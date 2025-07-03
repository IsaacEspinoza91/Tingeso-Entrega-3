import { useEffect, useState } from 'react';
import { getComprobanteByIdReserva } from '../../services/comprobanteService';
import { getIntegrantes } from '../../services/reservaService';
import './ReservaDetalle.css';
import {
    FaSpinner,
    FaEnvelope,
    FaEdit,
    FaCalendarAlt,
    FaClock,
    FaUser,
    FaUsers,
    FaClipboardList,
    FaIdBadge,
    FaPhone,
    FaEnvelopeOpenText,
    FaMoneyBill, FaMoneyCheckAlt,
    FaCalendarCheck,
    FaIdCard,
    FaExclamationCircle,
    FaRegUserCircle,
    FaFileAlt
} from 'react-icons/fa';
import PDFDownloadButton from './ComprobantePDF/PDFDownloadButton';
import EmailForm from './ComprobantePDF/EmailForm';

const ReservaDetalle = ({ reserva, onClose }) => {
    const [comprobante, setComprobante] = useState(null);
    const [integrantes, setIntegrantes] = useState([]);
    const [mostrarEmailPanel, setMostrarEmailPanel] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const comp = await getComprobanteByIdReserva(reserva.id);
                setComprobante(comp);
                const ints = await getIntegrantes(reserva.id);
                setIntegrantes(ints);
            } catch (error) {
                console.error('Error al cargar datos:', error);
            } finally {
                setIsLoading(false);
            }
        };
        fetchData();
    }, [reserva.id]);

    if (isLoading) {
        return (
            <div className="modal-overlay">
                <div className="modal-content wide loading-modal">
                    <FaSpinner className="loading-icon" />
                    <p className="loading-text">Cargando datos de la reserva...</p>
                </div>
            </div>
        );
    }

    const estadoReservaClass = {
        confirmada: 'estado-confirmada',
        completada: 'estado-completada',
        cancelada: 'estado-cancelada'
    }[reserva.estado.toLowerCase()];

    return (
        <div className="modal-overlay">
            <div className="modal-content wide">
                <button className="close-btn" onClick={onClose}>×</button>

                <h2 className="section-title"><FaFileAlt /> DETALLE DE RESERVA #{reserva.id}</h2>
                <div className="reserva-info-bar">
                    <div className="detalle-reserva-grid">
                        <p><FaCalendarAlt /> <strong>Fecha:</strong> {reserva.fecha}</p>
                        <p><FaClock /> <strong>Horario:</strong> {reserva.horaInicio} - {reserva.horaFin}</p>
                        <p>
                            <FaExclamationCircle /> <strong>Estado:</strong>{' '}
                            <span className={estadoReservaClass}>{reserva.estado}</span>
                        </p>
                        <p><FaUser /> <strong>Cliente:</strong> {reserva.reservante.nombre} {reserva.reservante.apellido} ({reserva.reservante.id})</p>
                        <p><FaClipboardList /> <strong>Plan:</strong> {reserva.plan.descripcion} ({reserva.plan.id})</p>
                        <p><FaUsers /> <strong>Total Personas:</strong> {reserva.totalPersonas}</p>
                    </div>
                </div>

                <div className="integrantes-section">
                    <h4><FaUsers /> Integrantes</h4>
                    <div className="integrantes-list">
                        {integrantes.map((int) => (
                            <div key={int.id} className="integrante-card">
                                <p><FaIdBadge /> <strong>ID:</strong> {int.id}</p>
                                <p><FaRegUserCircle /><strong> Nombre:</strong> {int.nombre} {int.apellido}</p>
                                <p><FaIdCard /><strong> RUT:</strong> {int.rut}</p>
                                <p><FaEnvelopeOpenText /> <strong>Correo:</strong> {int.correo}</p>
                                <p><FaPhone /> <strong>Teléfono:</strong> {int.telefono}</p>
                            </div>
                        ))}
                    </div>
                </div>

                {comprobante && (
                    <div className="comprobante-section">
                        <div className="comprobante-header">
                            <h3 className="section-title"><FaMoneyCheckAlt /> Comprobante #{comprobante.id}</h3>
                            <div className="comprobante-actions">
                                <PDFDownloadButton comprobante={comprobante} />
                                <button className="btn-email" onClick={() => setMostrarEmailPanel(true)}>
                                    <FaEnvelope /> Enviar Comprobante
                                </button>
                                <button className="btn-editar"><FaEdit /> Editar</button>
                            </div>
                        </div>

                        <div className="comprobante-info-bar">
                            <div className={`estado-comprobante ${comprobante.pagado ? 'pagado' : 'pendiente'}`}>
                                {comprobante.pagado ? 'Pagado' : 'Pendiente'}
                            </div>
                            <div><strong><FaMoneyBill /> Total:</strong> ${comprobante.total.toLocaleString()}</div>
                            <div><strong><FaCalendarCheck /> Fecha:</strong> {reserva.fecha}</div>
                        </div>

                        <div className="tabla-scroll">
                            <table className="comprobante-detalles">
                                <thead>
                                    <tr>
                                        <th>Cliente</th>
                                        <th>Tarifa</th>
                                        <th>%Desc Gr</th>
                                        <th>Desc Grupo</th>
                                        <th>%Desc Esp</th>
                                        <th>Desc Especial</th>
                                        <th>Desc Extra</th>
                                        <th>Monto Final</th>
                                        <th>IVA</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {comprobante.detalles.map((det) => (
                                        <tr key={det.id}>
                                            <td>{det.cliente.nombre} {det.cliente.apellido}</td>
                                            <td>${det.tarifa}</td>
                                            <td>{det.porcentajeDescuentoGrupo}%</td>
                                            <td>${det.descuentoGrupo}</td>
                                            <td>{det.porcentajeDescuentoEspecial}%</td>
                                            <td>${det.descuentoEspecial}</td>
                                            <td>${det.descuentoExtra}</td>
                                            <td>${det.montoFinal}</td>
                                            <td>${det.montoIva}</td>
                                            <td>${det.montoTotal}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {mostrarEmailPanel && (
                    <EmailForm
                        comprobante={comprobante}
                        onClose={() => setMostrarEmailPanel(false)}
                    />
                )}
            </div>
        </div>
    );
};

export default ReservaDetalle;
