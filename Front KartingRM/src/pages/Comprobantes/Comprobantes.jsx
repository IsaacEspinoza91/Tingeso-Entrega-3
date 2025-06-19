import React from 'react';
import ComprobanteSearch from '../../components/comprobantes/ComprobanteSearch';
import './Comprobantes.css';

const Comprobantes = () => {
  return (
    <div className="comprobantes-page">
      <h1>Administración de Comprobantes</h1>
      <ComprobanteSearch />
    </div>
  );
};

export default Comprobantes;