import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

interface FilterState {
  grade?: number;
  semester?: number;
  page: number;
}

const initialState: FilterState = {
  grade: undefined,
  semester: undefined,
  page: 0,
};

const filterSlice = createSlice({
  name: "filters",
  initialState,
  reducers: {
    setFilters: (state, action: PayloadAction<Partial<FilterState>>) => {
      return { ...state, ...action.payload };
    },
    setPage: (state, action: PayloadAction<number>) => {
      state.page = action.payload;
    },
  },
});

export const { setFilters, setPage } = filterSlice.actions;
export default filterSlice.reducer;
