import '../src/index.css'
import './App.css'
import {
  createBrowserRouter,
  RouterProvider
} from 'react-router-dom'
import SelectMultiPage from './pages/SelectMultiPage'
import IndexPage from './pages/IndexPage'
import SelectSinglePage from './pages/SelectSinglePage'
import SettingMultiPage from './pages/SettingMultiPage'
import WaitingRoomPage from './pages/WaitingRoomPage'
import SinglePlay from './pages/SinglePage'
import Multiplay from './pages/MultiplayPage'
import WordListPage from './pages/WordListPage'

function App() {

  const router = createBrowserRouter([
    {
      path: "/",
      element: <IndexPage/>,
    },    
    {
      path: "/englising",
      children: [
        {
          path:"selectSingle",
          element: <SelectSinglePage /> 
        },
        {
          path:"selectMulti",
          element: <SelectMultiPage /> 
        },
        {
          path:"settingMulti",
          element: <SettingMultiPage /> 
        },
        {
          path:"wordList",
          element: <WordListPage /> 
        }
      ]
    },
    {
      path: "/singlePlay",
      element: <SinglePlay/>
    },
    {
      path: "/waitroom/:multiId",
      element: <WaitingRoomPage/>
    },
    {
      path: "/multiPlay/:multiId",
      element: <Multiplay/>
    },
  ])

  return (
    <div>
      <div>
      <RouterProvider router={router} fallbackElement={<p>Loading...</p>} />
      </div>
    </div>
  )
}

export default App