import { configureStore } from '@reduxjs/toolkit';
import userReducer from '../modules/login/redux/UserSlice';
import accountApi from './api/AccountApi';
import fileApi from './api/FileApi';
import homeApi from './api/HomeApi';

export const store = configureStore({
    reducer: {
        [accountApi.reducerPath]: accountApi.reducer,
        [fileApi.reducerPath]: fileApi.reducer,
        [homeApi.reducerPath]: homeApi.reducer,
        user: userReducer,
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(
            accountApi.middleware,
            fileApi.middleware,
        )
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch