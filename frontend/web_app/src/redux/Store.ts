import { configureStore } from '@reduxjs/toolkit';
import userReducer from '../modules/login/redux/UserSlice';

import accountApi from './api/AccountApi';
import fileApi from './api/FileApi';
import followApi from './api/FollowApi';
import homeApi from './api/HomeApi';

export const store = configureStore({
    reducer: {
        [accountApi.reducerPath]: accountApi.reducer,
        [fileApi.reducerPath]: fileApi.reducer,
        [homeApi.reducerPath]: homeApi.reducer,
        [followApi.reducerPath]: followApi.reducer,
      
        user: userReducer,
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(
            accountApi.middleware,
            fileApi.middleware,
            homeApi.middleware,
            followApi.middleware,
        )
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch