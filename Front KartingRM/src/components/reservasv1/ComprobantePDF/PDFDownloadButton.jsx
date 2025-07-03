import React from 'react';
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

export default PDFDownloadButton;