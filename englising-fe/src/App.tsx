import '../src/index.css'
import './App.css'
import {
  createBrowserRouter,
  RouterProvider
} from 'react-router-dom'
import SelectMultiPage from './pages/SelectMultiPage'
import IndexPage from './pages/IndexPage'
import SelectSingle1Page from './pages/SelectSingle1Page.tsx'
import SelectSingle2Page from './pages/SelectSingle2Page.tsx'
import SelectSingle3Page from './pages/SelectSingle3Page.tsx'
import SettingMultiPage from './pages/SettingMultiPage'
import WaitingRoomPage from './pages/WaitingRoomPage'
import SinglePlay from './pages/SinglePage'
import Multiplay from './pages/MultiplayPage'
import WordListPage from './pages/WordListPage'
import SidebarLayout from './pages/SidebarLayout'
import PlayerTest from './component/multi/PlayerTest.tsx'
import SearchResultPage from './pages/SearchResultPage.tsx'
import SingleResultPlage from './pages/SingleResultPage.tsx'

function App() {

  const router = createBrowserRouter([
    {
      path: "/",
      element: <IndexPage/>,
    },    
    {
      path: "/englising",
      element: <SidebarLayout />,
      children: [
        {
          path:"selectSingle1",
          element: <SelectSingle1Page />,
        },
        {
          path:"selectSingle2",
          element: <SelectSingle2Page />,
        },
        {
          path:"selectSingle3",
          element: <SelectSingle3Page />,
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
        },
        {path:"searchResult/:searchTerm",
          element: <SearchResultPage/>
        }
      ]
    },
    {
      path: "/singlePlay/:trackId/:level/:youtubeId",
      element: <SinglePlay/>
    },
    {
      path: "/SinglePlay/result/:singlePlayId",
      element: <SingleResultPlage/>
    },
    {
      path: "/waitroom/:multiId",
      element: <WaitingRoomPage/>
    },
    {
      path: "/multiPlay/:multiId",
      element: <Multiplay/>
    },
    {
      path: "/multiPlay/playerTest",
      element: <PlayerTest/>
    }
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