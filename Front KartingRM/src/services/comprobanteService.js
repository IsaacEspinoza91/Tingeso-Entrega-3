import httpClient from '../http-common';

const URL_LOCAL = '/api/reservas-comprobantes-service';

// Peticion Get para obtener comprobante segun id
export const getComprobanteById = async (idComprobante) => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/comprobantes/${idComprobante}`);
    return response.data;
  } catch (error) {
    console.error(`Error al obtener comprobante con ID ${idComprobante}:`, error);
    throw error;
  }
};

// Peticion Get para obtener comprobante segun id reserva
export const getComprobanteByIdReserva = async (idReserva) => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/comprobantes/reserva/${idReserva}`);
    return response.data;
  } catch (error) {
    console.error(error.message);
    throw error;
  }
};

// Peticion Post para crear comprobante segun id de reserva y descuento extra
export const createComprobante = async (idReserva, descuentoExtra) => {
  try {
    const response = await httpClient.post(
      `${URL_LOCAL}/comprobantes/reserva/${idReserva}/descuento-extra/${descuentoExtra}`
    );
    return response.data;
  } catch (error) {
    console.error(error.message);
    throw error;
  }
};

// Peticion PUT para update de comprobante
export const updateComprobante = async (idComprobante, pagadoBool) => {
  try {
    const response = await httpClient.put(`${URL_LOCAL}/comprobantes/${idComprobante}/estado/${pagadoBool}`);
    return response.data;
  } catch (error) {
    console.error(`Error al actualizar comprobante con ID ${idComprobante}:`, error);
    throw error;
  }
};

// Peticion Delete de comprobante segun id
export const deleteComprobante = async (idComprobante) => {
  try {
    await httpClient.delete(`${URL_LOCAL}/comprobantes/${idComprobante}`);
    return idComprobante;
  } catch (error) {
    console.error(`Error al eliminar comprobante con ID ${idComprobante}:`, error);
    throw error;
  }
};
