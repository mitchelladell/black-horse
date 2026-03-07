export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  errorCode: string;
}

export class ApiResponseError extends Error implements ApiError {
  timestamp!: string;
  status!: number;
  error!: string;
  path!: string;
  errorCode!: string;

  constructor(body: ApiError) {
    super(body.message);
    Object.assign(this, body);
  }
}
