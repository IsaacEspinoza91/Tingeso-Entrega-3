import httpClient from '../http-common';

const URL_LOCAL = '/api/rack-semanal-service/get-rack';

export const getReservasSemana = async (semanaOffset = 0) => {
  try {
    // semanaOffset indica de que semana obtener las reservas.
    //  0 -> semana actual
    // -1 -> semana anterior
    //  1 -> semana siguiente
    const response = await httpClient.get(`${URL_LOCAL}/${semanaOffset}`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener reservas semanales:', error);
    throw error;
  }
};