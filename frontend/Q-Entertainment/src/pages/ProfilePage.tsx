import { useParams } from "react-router-dom";

export default function ProfilePage() {

    const param = useParams<{profileId : string}>();
    console.log(param);

   return (
    <div>
        <h1>Profile Page {param.profileId}</h1>
    </div>
   );
}