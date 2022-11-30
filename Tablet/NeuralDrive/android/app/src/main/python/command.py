from enum import Enum

class Action(str, Enum):
    GET_VIZUALIZATIONS = 'GET_VIZUALIZATIONS'
    EXECUTE_QUERY = 'EXECUTE_QUERY'
    GET_WATCH_DATA = 'GET_WATCH_DATA'
    SAVE_SESSION_LOCAL = 'SAVE_SESSION_LOCAL'
    GET_SESSION_BY_ID= 'GET_SESSION_BY_ID'
    GET_SESSION_INFO = 'GET_SESSION_INFO'
    DELETE_SESSIONS= 'DELETE_SESSIONS'
    RECEIVE_DATA_WATCH = 'RECEIVE_DATA_WATCH'
    START_SESSION = 'START_SESSION'
    SAVE_SESSION_LOCAL_TABLET = 'SAVE_SESSION_LOCAL_TABLET'
    EXPORT_SESSION_TO_DISTANT_SERVER = 'EXPORT_SESSION_TO_DISTANT_SERVER'
    STOP_SESSION = 'STOP_SESSION'

class Session_status(Enum):
    STOP = 0
    START = 1
    



