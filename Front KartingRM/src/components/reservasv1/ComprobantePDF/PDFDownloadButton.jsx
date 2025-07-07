import React from 'react';
import PropTypes from 'prop-types';
import { PDFDownloadLink } from '@react-pdf/renderer';
import ComprobantePDF from './ComprobantePDF';
import { FaFilePdf } from 'react-icons/fa';
import '../ReservaDetalle.css';

const PDFDownloadButton = ({ comprobante }) => {
  return (
    <PDFDownloadLink
      className="btn btn-pdf"
      document={<ComprobantePDF comprobante={comprobante} />}
      fileName={`Comprobante reserva #${comprobante.reserva.id}.pdf`}
    >
      {({ loading }) => (
        <>
          <FaFilePdf />
          {loading ? ' Generando...' : ' Descargar PDF'}
        </>
      )}
    </PDFDownloadLink>
  );
};

// Validación de PropTypes
PDFDownloadButton.propTypes = {
  comprobante: PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    reserva: PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired
    }).isRequired,
    pagado: PropTypes.bool.isRequired,
    total: PropTypes.number.isRequired,
    detalles: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
        cliente: PropTypes.shape({
          nombre: PropTypes.string.isRequired,
          apellido: PropTypes.string.isRequired
        }).isRequired,
        tarifa: PropTypes.number.isRequired,
        porcentajeDescuentoGrupo: PropTypes.number.isRequired,
        descuentoGrupo: PropTypes.number.isRequired,
        porcentajeDescuentoEspecial: PropTypes.number.isRequired,
        descuentoEspecial: PropTypes.number.isRequired,
        montoFinal: PropTypes.number.isRequired,
        montoIva: PropTypes.number.isRequired,
        montoTotal: PropTypes.number.isRequired
      })
    ).isRequired
  }).isRequired
};

export default PDFDownloadButton;