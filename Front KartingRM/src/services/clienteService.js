import httpClient from '../http-common';

const BASE_URL = '/api/cliente-service/cliente';

// Petición GET para obtener todos los clientes
export const getClientes = async () => {
  try {
    const response = await httpClient.get(`${BASE_URL}/`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener todos los clientes:', error);
    throw error;
  }
};

// Petición GET para obtener solo clientes activos
export const getClientesActivos = async () => {
  try {
    const response = await httpClient.get(`${BASE_URL}/activos`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener clientes activos:', error);
    throw error;
  }
};

// Petición GET para obtener solo clientes inactivos
export const getClientesInactivos = async () => {
  try {
    const response = await httpClient.get(`${BASE_URL}/inactivos`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener clientes inactivos:', error);
    throw error;
  }
};

// Petición GET para obtener cliente por ID
export const getClienteById = async (id) => {
  try {
    const response = await httpClient.get(`${BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error al obtener cliente con ID ${id}:`, error);
    throw error;
  }
};

// Petición GET para obtener cliente por RUT
export const getClienteByRut = async (rut) => {
  try {
    const response = await httpClient.get(`${BASE_URL}/rut/${rut}`);
    return response.data;
  } catch (error) {
    console.error(`Error al obtener cliente con RUT ${rut}:`, error);
    throw error;
  }
};

// Petición GET para obtener clientes por nombre y apellido
export const getClientesByNombreApellido = async (nombre, apellido) => {
  try {
    const response = await httpClient.get(`${BASE_URL}/nombre/${nombre}/apellido/${apellido}`);
    return response.data;
  } catch (error) {
    console.error(`Error al buscar clientes por nombre ${nombre} y apellido ${apellido}:`, error);
    throw error;
  }
};

// Petición POST para crear un nuevo cliente
export const createCliente = async (clienteData) => {
  try {
    const response = await httpClient.post(`${BASE_URL}/`, clienteData);
    return response.data;
  } catch (error) {
    console.error('Error al crear cliente:', error);
    throw error;
  }
};

// Petición PUT para actualizar un cliente
export const updateCliente = async (id, clienteData) => {
  try {
    const response = await httpClient.put(`${BASE_URL}/${id}`, clienteData);
    return response.data;
  } catch (error) {
    console.error(`Error al actualizar cliente con ID ${id}:`, error);
    throw error;
  }
};

// Petición DELETE para eliminar un cliente
export const deleteCliente = async (id) => {
  try {
    await httpClient.delete(`${BASE_URL}/${id}`);
    return id; // Retornamos el ID eliminado para referencia
  } catch (error) {
    console.error(`Error al eliminar cliente con ID ${id}:`, error);
    throw error;
  }
};

// Petición PUT para desactivar un cliente
export const desactivarCliente = async (id) => {
  try {
    const response = await httpClient.put(`${BASE_URL}/inactivate/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error al desactivar cliente con ID ${id}:`, error);
    throw error;
  }
};

// Petición PUT para activar un cliente
export const activarCliente = async (id) => {
  try {
    const response = await httpClient.put(`${BASE_URL}/activate/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error al activar cliente con ID ${id}:`, error);
    throw error;
  }
};