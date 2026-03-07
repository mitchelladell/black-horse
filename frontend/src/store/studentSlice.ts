import { createSlice } from "@reduxjs/toolkit";

interface StudentState {
  studentId: number;
}

const initialState: StudentState = { studentId: 101 };

const studentSlice = createSlice({
  name: "student",
  initialState,
  reducers: {
    setStudentId: (state, action) => {
      state.studentId = action.payload;
    },
  },
});

export const { setStudentId } = studentSlice.actions;
export default studentSlice.reducer;
