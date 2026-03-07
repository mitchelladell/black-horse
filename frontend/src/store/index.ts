import { configureStore } from "@reduxjs/toolkit";
import filterReducer from "./filterSlice";
import studentReducer from "./studentSlice";

export const store = configureStore({
  reducer: {
    filters: filterReducer,
    student: studentReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
