/// <reference types="vitest/globals" />
import { render, screen } from "@testing-library/react";
import { Provider } from "react-redux";
import { configureStore } from "@reduxjs/toolkit";
import { PaginationControls } from "./PaginationControls";
import filterReducer from "../../store/filterSlice";

const createStore = (page: number) =>
  configureStore({
    reducer: { filters: filterReducer },
    preloadedState: {
      filters: { page, grade: undefined, semester: undefined },
    },
  });

describe("PaginationControls", () => {
  it("disables Prev button on page 0", () => {
    render(
      <Provider store={createStore(0)}>
        <PaginationControls isLastPage={false} />
      </Provider>,
    );
    expect(screen.getByText("« Prev")).toBeDisabled();
  });

  it("disables Next button on last page", () => {
    render(
      <Provider store={createStore(0)}>
        <PaginationControls isLastPage={true} />
      </Provider>,
    );
    expect(screen.getByText("Next »")).toBeDisabled();
  });

  it("shows correct page number", () => {
    render(
      <Provider store={createStore(2)}>
        <PaginationControls isLastPage={false} />
      </Provider>,
    );
    expect(screen.getByText("Page 3")).toBeInTheDocument();
  });
});
