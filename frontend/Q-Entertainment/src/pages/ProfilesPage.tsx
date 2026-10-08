import { Link } from "react-router-dom";

export default function ProfilesPage() {
    const profiles = [1, 2, 3, 4, 5];

    return (
        <>
            <div className='flex flex-col gap-2'>
                {profiles.map((profile) => (
                    <Link key={profile} to={`/profilepage/${profile}`}>
                        Profile {profile}
                    </Link>
                ))}
            </div>
        </>
    );
}