export interface Command {
  action: Action;
  arg: any;
}

export enum Action {
  START_SESSION = 0,
  CONNECT_WATCH = 1,
  RECEIVE_DATA_WATCH = 2,
  EXECUTE_QUERY = 3,
}
