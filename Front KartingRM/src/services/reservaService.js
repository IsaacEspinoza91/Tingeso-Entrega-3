import httpClient from '../http-common';

const URL_LOCAL = '/api/reservas-comprobantes-service'

// Peticion GET de todas las reservas
export const getReservas = async () => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/reservas/DTO/`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener reservas:', error);
    throw error;
  }
};

// Peticion Get de reserva segun id
export const getReservaById = async (idReserva) => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/reservas/DTO/${idReserva}`);
    return response.data;
  } catch (error) {
    console.error(`Error al obtener reserva con ID ${idReserva}:`, error);
    throw error;
  }
};

// Peticion Get de reservas de un cliente segun id
export const getReservasByCliente = async (idCliente) => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/reservas/DTO/reservante/${idCliente}`);
    return response.data;
  } catch (error) {
    console.error(`Error al obtener reservas del cliente ${idCliente}:`, error);
    throw error;
  }
};

// Peticion POST para crear reserva
export const createReserva = async (reservaData) => {
  try {
    const response = await httpClient.post(`${URL_LOCAL}/reservas/`, reservaData);
    return response.data;
  } catch (error) {
    console.error('Error al crear reservaxx:', error);
    throw error;
  }
};

// Peticion PUT para update de reserva segun cuerpo
export const updateReserva = async (idReserva, reservaData) => {
  try {
    const response = await httpClient.put(`${URL_LOCAL}/reservas/${idReserva}`, reservaData);
    return response.data;
  } catch (error) {
    console.error(`Error al actualizar reserva con ID ${idReserva}:`, error);
    throw error;
  }
};

// Peticion POST para relacionar cliente integrante con reserva
export const addIntegrante = async (idReserva, idCliente) => {
  try {
    const response = await httpClient.post(`${URL_LOCAL}/cliente-reserva/agregar/cliente/${idCliente}/reserva/${idReserva}`);
    return response.data;
  } catch (error) {
    console.error(`Error al agregar integrante a reserva ${idReserva}:`, error);
    throw error;
  }
};

// Peticion DELETE para quitar un cliente como integrante de una reserva
export const removeIntegrante = async (idReserva, idCliente) => {
  try {
    const response = await httpClient.delete(`${URL_LOCAL}/cliente-reserva/quitar/cliente/${idCliente}/reserva/${idReserva}`);
    return response.data;
  } catch (error) {
    console.error(`Error al quitar integrante de reserva ${idReserva}:`, error);
    throw error;
  }
};

// Peticion DELETE para eliminar una reserva
export const deleteReserva = async (idReserva) => {
  try {
    await httpClient.delete(`${URL_LOCAL}/reservas/${idReserva}`);
    return idReserva;
  } catch (error) {
    console.error(`Error al eliminar reserva con ID ${idReserva}:`, error);
    throw error;
  }
};

// GET de integrantes by id Reserva
export const getIntegrantes = async (idReserva) => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/cliente-reserva/ClienteDTO/reserva/${idReserva}`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener integrantes:', error);
    throw error;
  }
};