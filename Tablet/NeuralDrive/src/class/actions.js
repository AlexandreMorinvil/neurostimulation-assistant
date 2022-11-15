export const Action = {
  START_SESSION: 0,
  CONNECT_WATCH: 1,
  RECEIVE_DATA_WATCH: 2,
  EXECUTE_QUERY: 3,
  GET_WATCH_DATA: 4,
  SAVE_SESSION: 5,
  DELETE_SESSION: 6,
  GET_ALL_SESSION: 7,
};

export const Status = {
  IDLE: 3,
  STOP: 0,
  START: 1,
};

export const ERROR_CODE = {
  FAIL_CONNECT_TO_SERVER: -1,
};
