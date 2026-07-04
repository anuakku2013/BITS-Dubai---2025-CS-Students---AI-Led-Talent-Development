package com.example.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Storage
import androidx.compose.ui.graphics.vector.ImageVector
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AIFeedItem(
    val id: String,
    val title: String,
    val source: String,
    val date: String,
    val description: String,
    val tags: List<String> = emptyList(),
    val location: String = "Dubai, UAE",
    val link: String = "https://www.dubaifuture.ae/"
)

data class AIEvent(
    val id: String,
    val title: String,
    val organizer: String,
    val date: String,
    val location: String,
    val description: String,
    val imageUrl: String,
    val externalLink: String,
    val isVirtual: Boolean = false,
    val category: String = "Conference"
)

data class AIInternship(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val duration: String,
    val type: String, // "Remote" or "Dubai Local" or "Hybrid"
    val description: String,
    val requirements: List<String>,
    val freeCourseLinks: List<String>,
    val applyLink: String,
    val difficulty: String = "Intermediate",
    val deadline: String = "Oct 30, 2026"
)

data class AIJob(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val experienceRequired: String, // "0-1 Years", "Fresh Graduate"
    val salaryEstimate: String,
    val skillsNeeded: List<String>,
    val description: String,
    val applyLink: String
)

data class FreeCourse(
    val id: String,
    val title: String,
    val provider: String, // e.g. "Stanford / DeepLearning.AI", "Google Cloud"
    val duration: String,
    val rating: Double,
    val link: String,
    val category: String, // "Math", "Programming", "Machine Learning", "Generative AI"
    val description: String
)

data class HotSkill(
    val id: String,
    val name: String,
    val description: String,
    val importanceScore: Double, // out of 10.0
    val salaryBoost: String,
    val recommendedCourses: List<String>, // List of Course IDs
    val projectIdea: String,
    val iconName: String
)

data class CareerPath(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val targetRole: String,
    val startingSalaryDubai: String,
    val requiredCourses: List<String>, // List of Course IDs
    val coreSkills: List<String>,
    val finalProjectSuggestion: String
)

object CareerData {
    val courses = listOf(
        FreeCourse(
            id = "cs50_ai",
            title = "CS50's Introduction to Artificial Intelligence with Python",
            provider = "Harvard University (edX)",
            duration = "7 weeks (10-30 hours/week)",
            rating = 4.9,
            link = "https://www.edx.org/course/cs50s-introduction-to-artificial-intelligence-with-python",
            category = "Foundation",
            description = "Learn the foundations of AI, search algorithms, probability, neural networks, and NLP in Python."
        ),
        FreeCourse(
            id = "dl_ai_everyone",
            title = "AI For Everyone",
            provider = "DeepLearning.AI (Coursera Free Audit)",
            duration = "6 hours",
            rating = 4.8,
            link = "https://www.coursera.org/learn/ai-for-everyone",
            category = "Foundation",
            description = "A non-technical introduction to AI concepts, machine learning pipelines, and building AI strategies in organizations."
        ),
        FreeCourse(
            id = "ml_specialization",
            title = "Machine Learning Specialization",
            provider = "Stanford University & DeepLearning.AI",
            duration = "2 months (10 hours/week)",
            rating = 4.9,
            link = "https://www.coursera.org/specializations/machine-learning-introduction",
            category = "Machine Learning",
            description = "The gold standard course taught by Andrew Ng. Covers supervised learning (linear regression, logistic, neural networks) and unsupervised learning."
        ),
        FreeCourse(
            id = "google_genai_fun",
            title = "Generative AI Fundamentals Specialization",
            provider = "Google Cloud",
            duration = "10 hours",
            rating = 4.7,
            link = "https://www.cloudskillsboost.google/paths/118",
            category = "Generative AI",
            description = "Covers Large Language Models (LLMs), Image Generation, Attention Mechanisms, and Generative AI Studio on Vertex AI."
        ),
        FreeCourse(
            id = "fast_ai_dl",
            title = "Practical Deep Learning for Coders",
            provider = "Fast.ai",
            duration = "36 hours",
            rating = 4.9,
            link = "https://course.fast.ai/",
            category = "Deep Learning",
            description = "An excellent hands-on, top-down deep learning course using PyTorch and fastai. Build state-of-the-art models immediately."
        ),
        FreeCourse(
            id = "langchain_llms",
            title = "LangChain for LLM Application Development",
            provider = "DeepLearning.AI",
            duration = "1 hour",
            rating = 4.8,
            link = "https://www.deeplearning.ai/short-courses/langchain-for-llm-application-development/",
            category = "Generative AI",
            description = "Learn how to use LangChain to call LLMs, create prompts, manage memory, and build conversational agents."
        ),
        FreeCourse(
            id = "hf_nlp",
            title = "Hugging Face NLP Course",
            provider = "Hugging Face",
            duration = "20 hours",
            rating = 4.8,
            link = "https://huggingface.co/learn/nlp-course/",
            category = "Generative AI",
            description = "Master transformer models, fine-tuning pretrained models on datasets, and deploying models using Hugging Face Spaces."
        ),
        FreeCourse(
            id = "mlops_zoomcamp",
            title = "MLOps Zoomcamp",
            provider = "DataTalks.Club (GitHub)",
            duration = "10 weeks",
            rating = 4.8,
            link = "https://github.com/DataTalksClub/mlops-zoomcamp",
            category = "MLOps",
            description = "Build a complete real-world MLOps pipeline covering Experiment tracking, orchestration, deployment, and monitoring."
        )
    )

    val events = listOf(
        AIEvent(
            id = "evt_dubai_ai_web3",
            title = "Dubai AI & Web3 Festival 2026",
            organizer = "Dubai International Financial Centre (DIFC)",
            date = "Sept 14-15, 2026",
            location = "Madinat Jumeirah, Dubai",
            description = "The premier global event charting the future of AI, web3, and digital economy. Featuring global tech leaders, hackathons, and student demo pitches.",
            imageUrl = "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=500&auto=format&fit=crop&q=60",
            externalLink = "https://www.dubaiaiweb3festival.com/"
        ),
        AIEvent(
            id = "evt_dubai_assembly",
            title = "Dubai Assembly for Generative AI",
            organizer = "Dubai Future Foundation",
            date = "Oct 11-12, 2026",
            location = "Museum of the Future & AREA 2071, Dubai",
            description = "A massive gathering focused on LLMs, generative creative arts, regulatory frameworks, and practical applications in government and industry.",
            imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=500&auto=format&fit=crop&q=60",
            externalLink = "https://www.dubaifuture.ae/"
        ),
        AIEvent(
            id = "evt_gitex_supernova",
            title = "GITEX Global - AI Supernova Stage",
            organizer = "Dubai World Trade Centre (DWTC)",
            date = "Oct 18-22, 2026",
            location = "DWTC, Dubai",
            description = "The world's largest tech show dedicated to AI enterprises. Features student research paper presentations, global expert debates, and hiring pavilions.",
            imageUrl = "https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=500&auto=format&fit=crop&q=60",
            externalLink = "https://www.gitex.com/"
        ),
        AIEvent(
            id = "evt_dubai_hack",
            title = "DIFC Youth AI Hackathon",
            organizer = "DIFC Innovation Hub",
            date = "Nov 05-07, 2026",
            location = "Area 2071, Boulevard Emirates Towers, Dubai",
            description = "A 48-hour collaborative hackathon for university students in the UAE to build AI solutions for financial services, retail, and sustainability.",
            imageUrl = "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=500&auto=format&fit=crop&q=60",
            externalLink = "https://difc.ae/innovation-hub"
        ),
        AIEvent(
            id = "evt_rit_symposium",
            title = "Annual AI & Robotics Research Symposium",
            organizer = "Rochester Institute of Technology (RIT) Dubai",
            date = "Dec 03, 2026",
            location = "RIT Dubai Campus, Silicon Oasis",
            description = "A university-led student-focused event sharing academic breakthroughs in Computer Vision, Reinforcement Learning, and Smart Cities.",
            imageUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=500&auto=format&fit=crop&q=60",
            externalLink = "https://www.rit.edu/dubai"
        )
    )

    val internships = listOf(
        AIInternship(
            id = "int_pwc_dubai",
            title = "Machine Learning & Generative AI Intern",
            company = "PwC Middle East AI Hub",
            location = "Emaar Square, Downtown Dubai",
            duration = "3 Months (Fall 2026)",
            type = "Hybrid (Dubai Local)",
            description = "Work alongside AI researchers to design enterprise search solutions utilizing RAG, evaluate LLM performance, and assist in client presentations.",
            requirements = listOf("Solid Python foundations", "Familiarity with LangChain/LlamaIndex", "Undergraduate in Computer Science (2nd/3rd year)"),
            freeCourseLinks = listOf("langchain_llms", "google_genai_fun"),
            applyLink = "https://www.pwc.com/me/en/careers",
            deadline = "Sep 30, 2026"
        ),
        AIInternship(
            id = "int_tii_abudhabi",
            title = "AI Research Intern (Natural Language Processing)",
            company = "Technology Innovation Institute (TII)",
            location = "Dubai Office / Abu Dhabi Masdar City",
            duration = "6 Months (Full/Part-Time)",
            type = "Hybrid (Dubai Local)",
            description = "Join the makers of Falcon LLM! Support fine-tuning experiments, data preprocessing for Arabic dialects, and benchmark evaluation.",
            requirements = listOf("Experience in PyTorch or Hugging Face", "Basic understanding of transformers", "Strong linear algebra & calculus basics"),
            freeCourseLinks = listOf("fast_ai_dl", "hf_nlp"),
            applyLink = "https://www.tii.ae/careers",
            deadline = "Oct 15, 2026"
        ),
        AIInternship(
            id = "int_emirates_group",
            title = "Software Engineer Intern - AI & Aviation Lab",
            company = "Emirates Group",
            location = "Emirates Group HQ, Dubai",
            duration = "2 Months (Summer/Winter)",
            type = "Dubai Local",
            description = "Develop prototypes utilizing computer vision for ground operations optimization or chatbot agents for customer experience.",
            requirements = listOf("Java/Kotlin or Python skills", "Familiarity with REST APIs", "Good presentation & communication skills"),
            freeCourseLinks = listOf("cs50_ai", "dl_ai_everyone"),
            applyLink = "https://www.emiratesgroupcareers.com",
            deadline = "Oct 25, 2026"
        ),
        AIInternship(
            id = "int_virtual_g42",
            title = "Virtual AI & Data Engineering Intern",
            company = "G42 Cloud (Inception AI)",
            location = "Online / Remote (UAE Targeted)",
            duration = "2 Months",
            type = "Remote",
            description = "A self-paced, mentor-guided virtual experience building dataset scraping pipelines, indexing vector databases, and constructing prompt templates.",
            requirements = listOf("Python script writing", "Familiarity with GitHub", "Passion for generative AI tools"),
            freeCourseLinks = listOf("google_genai_fun", "langchain_llms"),
            applyLink = "https://g42.ai/careers",
            deadline = "Nov 01, 2026"
        ),
        AIInternship(
            id = "int_startup_difc",
            title = "Prompt Engineer & AI Integration Intern",
            company = "CognitiveLabs (DIFC Fintech Startup)",
            location = "FinTech Hive, DIFC, Dubai",
            duration = "3 Months",
            type = "Dubai Local",
            description = "Help a seed-stage fintech startup integrate OpenAI and Claude APIs. Benchmark system prompts, craft vector embeddings, and build clean frontends.",
            requirements = listOf("JSON, REST APIs", "HTML/CSS or Mobile App basics", "Highly creative in English writing & logic testing"),
            freeCourseLinks = listOf("dl_ai_everyone", "langchain_llms"),
            applyLink = "https://difc.ae/innovation-hub",
            deadline = "Nov 15, 2026"
        )
    )

    val jobs = listOf(
        AIJob(
            id = "job_anghami",
            title = "Junior Recommendation Systems Analyst",
            company = "Anghami",
            location = "Dubai Media City, Dubai",
            experienceRequired = "Fresh Graduate / 0-1 Years",
            salaryEstimate = "AED 12,000 - 16,000 / month",
            skillsNeeded = listOf("Python", "Collaborative Filtering", "SQL", "Pandas & NumPy"),
            description = "Work on the music recommendation algorithm. Analyze user skip patterns, train collaborative filtering models, and evaluate recommendation diversity.",
            applyLink = "https://careers.anghami.com/"
        ),
        AIJob(
            id = "job_careem",
            title = "Associate Data Scientist (AI Operations)",
            company = "Careem (Uber Subsidiary)",
            location = "Dubai Internet City, Dubai",
            experienceRequired = "Fresh Graduate / 0-2 Years",
            salaryEstimate = "AED 15,000 - 20,000 / month",
            skillsNeeded = listOf("Machine Learning", "Python", "A/B Testing", "Tableau/Data Visualization"),
            description = "Support the ETA routing algorithms and dynamic pricing dispatch engine. Run analytics, clean GPS logs, and assist senior data scientists in validation trials.",
            applyLink = "https://careers.careem.com/"
        ),
        AIJob(
            id = "job_ibm",
            title = "Graduate AI Developer - Consulting",
            company = "IBM Middle East",
            location = "Dubai Design District (d3), Dubai",
            experienceRequired = "Fresh Graduate (Class of 2025/2026)",
            salaryEstimate = "AED 14,000 - 18,000 / month",
            skillsNeeded = listOf("Python", "Cloud Computing (IBM/AWS)", "RAG Architectures", "Docker Basics"),
            description = "Participate in IBM's Consulting Graduate Program. Get certified in IBM watsonx, and assist in developing customized enterprise AI copilots for government clients.",
            applyLink = "https://www.ibm.com/careers"
        ),
        AIJob(
            id = "job_g42",
            title = "Associate Machine Learning Engineer",
            company = "G42 Healthcare / AI",
            location = "Dubai Healthcare City (DHCC), Dubai",
            experienceRequired = "0-1 Years / Strong Portfolio",
            salaryEstimate = "AED 18,000 - 24,000 / month",
            skillsNeeded = listOf("PyTorch", "Computer Vision", "Scikit-Learn", "Git & CI/CD"),
            description = "Support the development of healthcare imagery AI models. Standardize medical scan formats, train CNNs/ViTs, and document performance metrics for clinical approval.",
            applyLink = "https://g42.ai/careers"
        )
    )

    val hotSkills = listOf(
        HotSkill(
            id = "sk_rag",
            name = "Retrieval-Augmented Generation (RAG)",
            description = "Connecting LLMs to custom knowledge bases (PDFs, DBs) using vector search. This is the #1 most requested skill for enterprise AI implementation.",
            importanceScore = 9.8,
            salaryBoost = "+25% in Dubai market",
            recommendedCourses = listOf("langchain_llms", "google_genai_fun"),
            projectIdea = "Build a 'Dubai CS Study Assistant' chatbot that indexes 2nd-year university syllabi and answers questions with precise sources.",
            iconName = "Psychology"
        ),
        HotSkill(
            id = "sk_pytorch",
            name = "Deep Learning with PyTorch",
            description = "The industry-standard library for designing, training, and fine-tuning neural networks. Absolutely vital for research and custom models.",
            importanceScore = 9.5,
            salaryBoost = "+20% in Dubai market",
            recommendedCourses = listOf("fast_ai_dl", "ml_specialization"),
            projectIdea = "Train a custom Convolutional Neural Network (CNN) to recognize handwritten Arabic script or digitize local retail receipts.",
            iconName = "Code"
        ),
        HotSkill(
            id = "sk_vector_db",
            name = "Vector Database Indexing",
            description = "Familiarity with high-speed semantic search systems like Pinecone, ChromaDB, Qdrant, or Milvus. Essential for agentic memory and fast RAG.",
            importanceScore = 9.0,
            salaryBoost = "+15% in Dubai market",
            recommendedCourses = listOf("langchain_llms"),
            projectIdea = "Create an audio/song semantic search app by converting song lyrics to embeddings and saving them in ChromaDB.",
            iconName = "Storage"
        ),
        HotSkill(
            id = "sk_mlops",
            name = "MLOps & LLMOps Pipeline Dev",
            description = "Deploying and managing ML models in production. Moving from local notebooks to cloud endpoints with Docker, MLflow, and Kubernetes.",
            importanceScore = 8.8,
            salaryBoost = "+30% (Extremely rare skill for fresh grads)",
            recommendedCourses = listOf("mlops_zoomcamp"),
            projectIdea = "Package a sentiment analysis model as a FastAPI app in Docker, deploy it on AWS/Herioku, and setup automatic performance logs.",
            iconName = "CloudQueue"
        )
    )

    val paths = listOf(
        CareerPath(
            id = "path_gen_ai",
            title = "Generative AI Developer",
            subtitle = "Place yourself at the absolute cutting edge",
            description = "A path focused on building smart products, AI agents, and RAG systems using existing models, API pipelines, and specialized fine-tuning.",
            targetRole = "Generative AI Engineer / AI Prompt Developer",
            startingSalaryDubai = "AED 15,000 - 22,000 / month",
            requiredCourses = listOf("cs50_ai", "google_genai_fun", "langchain_llms", "hf_nlp"),
            coreSkills = listOf("Retrieval-Augmented Generation", "Prompt Engineering", "Hugging Face Transformers", "API Orchestration"),
            finalProjectSuggestion = "Create a fully functional Multi-Agent AI System that automatically scans Dubai job boards, summarizes matching entry-level AI jobs, and drafts customized CV cover letters."
        ),
        CareerPath(
            id = "path_ml_eng",
            title = "Machine Learning Engineer",
            subtitle = "Design the neural architectures of tomorrow",
            description = "For students who love mathematics, training models from scratch, data preprocessing, statistical validation, and optimization.",
            targetRole = "Associate Machine Learning Engineer / Data Scientist",
            startingSalaryDubai = "AED 16,000 - 25,000 / month",
            requiredCourses = listOf("ml_specialization", "fast_ai_dl", "hf_nlp", "mlops_zoomcamp"),
            coreSkills = listOf("PyTorch Deep Learning", "Feature Engineering", "Model Optimization", "Kubernetes & Cloud Deploy"),
            finalProjectSuggestion = "Train and evaluate a custom vision model that identifies smart Dubai traffic congestion using real-time camera feeds, deployed with an active API gateway."
        )
    )

    val defaultFeeds = listOf(
        AIFeedItem(
            id = "feed_1",
            title = "Dubai Future Foundation Announces 'Dubai AI Campus' Expansion",
            source = "Dubai Future Foundation (DFF)",
            date = "July 02, 2026",
            description = "The Dubai AI Campus at Area 2071 is expanding to accommodate over 500 global AI startups. The expansion includes dedicated hardware labs for university student research, free high-performance GPU clusters, and custom co-working zones for tech founders.",
            tags = listOf("Dubai AI Campus", "DFF", "Startups", "GPU Access"),
            location = "Area 2071, Emirates Towers, Dubai",
            link = "https://www.dubaifuture.ae/"
        ),
        AIFeedItem(
            id = "feed_2",
            title = "DIFC Innovation Hub Unveils AI & Web3 Venture Fund worth AED 100M",
            source = "DIFC Innovation Hub",
            date = "June 28, 2026",
            description = "A new venture fund of AED 100M is dedicated to accelerating early-stage AI startups and student-led deep tech prototypes in the GCC. Grants of up to AED 50,000 are available for university graduation projects incorporating unique AI models.",
            tags = listOf("Funding", "DIFC", "Student Grants", "Innovation"),
            location = "DIFC, Dubai",
            link = "https://difc.ae/innovation-hub"
        ),
        AIFeedItem(
            id = "feed_3",
            title = "MBZUAI and IBM Launch Joint Center of Excellence in Generative AI",
            source = "Mohamed bin Zayed University of Artificial Intelligence",
            date = "June 15, 2026",
            description = "The Mohamed bin Zayed University of Artificial Intelligence (MBZUAI) has partnered with IBM to launch a joint AI Research Lab focused on developing Arabic LLMs, carbon-efficient transformer architectures, and vision systems for autonomous transportation in the UAE.",
            tags = listOf("Research", "Partnership", "Arabic LLMs", "IBM"),
            location = "MBZUAI Campus, Abu Dhabi / Dubai Office",
            link = "https://mbzuai.ac.ae/"
        ),
        AIFeedItem(
            id = "feed_4",
            title = "Dubai Chambers Holds Seminar on 'AI for Retail & Smart Commerce'",
            source = "Dubai Chambers",
            date = "June 10, 2026",
            description = "An interactive workshop detailing how local businesses are utilizing predictive analytics, retail computer vision, and dynamic price models. A special segment is dedicated to CS students presenting retail tech projects.",
            tags = listOf("Seminar", "Retail Tech", "Networking"),
            location = "Dubai Chambers HQ, Deira, Dubai",
            link = "https://www.dubaichamber.com"
        ),
        AIFeedItem(
            id = "feed_5",
            title = "TII Falcon 3.0 Open-Source Foundation Models Released",
            source = "Technology Innovation Institute (TII)",
            date = "May 24, 2026",
            description = "The UAE's Technology Innovation Institute has officially released Falcon 3.0, a lightweight, highly efficient model optimized for edge devices and mobile compilation. It outperforms previous models in multi-lingual reasoning and Arabic dialect comprehension.",
            tags = listOf("Falcon LLM", "TII", "Open Source", "Edge AI"),
            location = "Masdar City, Abu Dhabi / Online",
            link = "https://www.tii.ae"
        )
    )
}

fun openLink(context: android.content.Context, url: String) {
    try {
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("AI Hub Link", url)
            clipboard.setPrimaryClip(clip)
            android.widget.Toast.makeText(context, "No browser found. Link copied to clipboard!", android.widget.Toast.LENGTH_LONG).show()
        } catch (clipEx: Exception) {
            android.widget.Toast.makeText(context, "Could not open or copy link: $url", android.widget.Toast.LENGTH_LONG).show()
        }
    }
}

